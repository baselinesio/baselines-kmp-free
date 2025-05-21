@file:OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)

package io.baselines.toolkit.initializer

import io.baselines.toolkit.coroutines.AppDispatchers
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain


class CompositeInitializerTest {

    private val asyncInitializers = mutableSetOf<Lazy<AsyncInitializer>>()
    private val coreInitializers = mutableSetOf<Lazy<Initializer>>()
    private val sut: CompositeInitializer = CompositeInitializer(
        asyncInitializersLazy = asyncInitializers,
        coreInitializersLazy = coreInitializers,
        appDispatchers = AppDispatchers(),
    )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        asyncInitializers.clear()
        coreInitializers.clear()
        Dispatchers.resetMain()
    }

    @Test
    fun init_followsListOrder_coreInitRunsFirst() {
        val initResults = mutableListOf<String>()
        val asyncInitializer1 = createMockAsyncInitializer(result = Result.success(Unit)) {
            initResults.add("async1")
        }
        val asyncInitializer2 = createMockAsyncInitializer(result = Result.success(Unit)) {
            initResults.add("async2")
        }
        asyncInitializers.addAll(arrayOf(asyncInitializer1, asyncInitializer2))

        val coreInitializer1 = createMockCoreInitializer(result = Result.success(Unit)) {
            initResults.add("core1")
        }
        val coreInitializer2 = createMockCoreInitializer(result = Result.success(Unit)) {
            initResults.add("core2")
        }
        coreInitializers.addAll(arrayOf(coreInitializer1, coreInitializer2))

        runTest {
            sut.resultFlow
                .onSubscription { sut.initialize() }
                .first()

            assertSame("core1", initResults[0])
            assertSame("core2", initResults[1])
            assertSame("async1", initResults[2])
            assertSame("async2", initResults[3])
        }
    }

    @Test
    fun init_returnsSuccess_whenEveryInitializerSucceeds() {
        val asyncInitializer = createMockAsyncInitializer(result = Result.success(Unit))
        asyncInitializers.add(asyncInitializer)

        val coreInitializer = createMockCoreInitializer(result = Result.success(Unit))
        coreInitializers.add(coreInitializer)

        runTest {
            val result = sut.resultFlow
                .onSubscription { sut.initialize() }
                .first()

            assertTrue { result.isSuccess }
        }
    }

    @Test
    fun init_returnsFailure_whenCoreInitFails() {
        val asyncInitializer = createMockAsyncInitializer(result = Result.success(Unit))
        asyncInitializers.add(asyncInitializer)

        val coreInitializer = createMockCoreInitializer<Unit>(
            Result.failure(RuntimeException("Test: expected error"))
        )
        coreInitializers.add(coreInitializer)

        runTest {
            val result = sut.resultFlow
                .onSubscription { sut.initialize() }
                .first()

            assertTrue { result.isFailure }
        }
    }

    @Test
    fun init_returnsFailure_whenAsyncInitFails() {
        val asyncInitializer = createMockAsyncInitializer<Unit>(
            Result.failure(RuntimeException("Test: expected error"))
        )
        asyncInitializers.add(asyncInitializer)

        val coreInitializer = createMockCoreInitializer(Result.success(Unit))
        coreInitializers.add(coreInitializer)

        runTest {
            val result = sut.resultFlow
                .onSubscription { sut.initialize() }
                .first()

            assertTrue { result.isFailure }
        }
    }

    private fun <T> createMockAsyncInitializer(
        result: Result<T>,
        initCallback: () -> Unit = {},
    ): Lazy<AsyncInitializer> {
        return lazy {
            object : AsyncInitializer {
                override suspend fun init() {
                    delay(Random.nextLong(from = 0, until = 30))
                    initCallback.invoke()
                    result.onFailure {
                        throw it
                    }
                }
            }
        }
    }

    private fun <T> createMockCoreInitializer(
        result: Result<T>,
        initCallback: () -> Unit = {},
    ): Lazy<Initializer> {
        return lazy {
            object : Initializer {
                override suspend fun init() {
                    delay(Random.nextLong(from = 0, until = 30))
                    initCallback.invoke()
                    result.onFailure {
                        throw it
                    }
                }
            }
        }
    }
}
