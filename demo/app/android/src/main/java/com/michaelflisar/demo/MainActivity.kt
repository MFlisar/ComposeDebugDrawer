package com.michaelflisar.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.michaelflisar.composedebugdrawer.demo.BuildConfig
import com.michaelflisar.demo.classes.DemoLogging
import com.michaelflisar.lumberjack.core.L

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            DemoApp(
                prefs = DemoApp.PREFS,
                fileLoggingSetup = DemoLogging.fileLoggingSetup,
                isDebugBuild = BuildConfig.DEBUG
            )
        }
    }
}
