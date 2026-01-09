package io.baselines.sample.compose.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dev.zacsweers.metro.asContribution
import io.baselines.sample.compose.di.AndroidUiComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

open class BaselineActivity : ComponentActivity() {

    private val uiComponent by lazy {
        application.appComponent.asContribution<AndroidUiComponent.Factory>().createUiComponent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applySplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            uiComponent.composeApp()
        }
    }

    private fun applySplashScreen() {
        var appInitialized = false
        lifecycleScope.launch {
            application.appComponent.compositeInitializer.resultFlow.first()
            appInitialized = true
        }
        installSplashScreen().apply {
            setKeepOnScreenCondition { !appInitialized }
        }
    }
}
