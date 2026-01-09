@file:OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)

package io.baselines.toolkit.initializer

import dev.zacsweers.metro.Provider
import io.baselines.toolkit.coroutines.AppDispatchers
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest


class CompositeInitializerTest {

    private val asyncInitializers = mutableMapOf<Int, Provider<AsyncInitializer>>()
    private val coreInitializers = mutableMapOf<Int, Provider<Initializer>>()

    @AfterTest
    fun tearDown() {
        asyncInitializers.clear()
        coreInitializers.clear()
    }

    @Test
    fun init_followsListOrder_coreInitRunsFirst() = runTest {
        /* Given 1 */
        val sut = createSut(testScheduler)
        val initResults = mutableListOf<String>()
        val asyncInitializer1 = createMockAsyncInitializer(result = Result.success(Unit)) {
            initResults.add("async1")
        }
        val asyncInitializer2 = createMockAsyncInitializer(result = Result.success(Unit)) {
            initResults.add("async2")
        }
        asyncInitializers.putAll(arrayOf(0 to asyncInitializer1, 1 to asyncInitializer2))

        /* Given 2 */
        val coreInitializer1 = createMockCoreInitializer(result = Result.success(Unit)) {
            initResults.add("core1")
        }
        val coreInitializer2 = createMockCoreInitializer(result = Result.success(Unit)) {
            initResults.add("core2")
        }
        coreInitializers.putAll(arrayOf(3 to coreInitializer2, 2 to coreInitializer1))

        /* When */
        sut.resultFlow
            .onSubscription { sut.initialize() }
            .first()

        /* Then */
        assertSame("core1", initResults[0])
        assertSame("core2", initResults[1])
        assertSame("async1", initResults[2])
        assertSame("async2", initResults[3])
    }

    @Test
    fun init_returnsSuccess_whenEveryInitializerSucceeds() = runTest {
        /* Given 1 */
        val sut = createSut(testScheduler)
        val asyncInitializer = createMockAsyncInitializer(result = Result.success(Unit))
        asyncInitializers[0] = asyncInitializer

        /* Given 2 */
        val coreInitializer = createMockCoreInitializer(result = Result.success(Unit))
        coreInitializers[1] = coreInitializer

        /* When */
        val result = sut.resultFlow
            .onSubscription { sut.initialize() }
            .first()

        /* Then */
        assertTrue { result.isSuccess }
    }

    @Test
    fun init_returnsFailure_whenCoreInitFails() = runTest {
        /* Given 1 */
        val sut = createSut(testScheduler)
        val asyncInitializer = createMockAsyncInitializer(result = Result.success(Unit))
        asyncInitializers[0] = asyncInitializer

        /* Given 2 */
        val coreInitializer = createMockCoreInitializer<Unit>(
            Result.failure(RuntimeException("Test: expected error"))
        )
        coreInitializers[1] = coreInitializer

        /* When */
        val result = sut.resultFlow
            .onSubscription { sut.initialize() }
            .first()

        /* Then */
        assertTrue { result.isFailure }
    }

    @Test
    fun init_returnsFailure_whenAsyncInitFails() = runTest {
        /* Given 1 */
        val sut = createSut(testScheduler)
        val asyncInitializer = createMockAsyncInitializer<Unit>(
            Result.failure(RuntimeException("Test: expected error"))
        )
        asyncInitializers[0] = asyncInitializer

        /* Given 2 */
        val coreInitializer = createMockCoreInitializer(Result.success(Unit))
        coreInitializers[1] = coreInitializer

        /* When */
        val result = sut.resultFlow
            .onSubscription { sut.initialize() }
            .first()

        /* Then */
        assertTrue { result.isFailure }
    }

    private fun createSut(scheduler: TestCoroutineScheduler): CompositeInitializer {
        val testDispatcher = StandardTestDispatcher(scheduler)
        return CompositeInitializer(
            asyncInitializerProviders = asyncInitializers,
            coreInitializerProviders = coreInitializers,
            appDispatchers = AppDispatchers(
                io = testDispatcher,
                main = testDispatcher,
                default = testDispatcher,
            ),
        )
    }

    private fun <T> createMockAsyncInitializer(
        result: Result<T>,
        initCallback: () -> Unit = {},
    ): Provider<AsyncInitializer> {
        return Provider {
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
    ): Provider<Initializer> {
        return Provider {
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
