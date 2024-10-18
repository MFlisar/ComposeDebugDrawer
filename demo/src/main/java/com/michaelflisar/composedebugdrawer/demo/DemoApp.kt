package com.michaelflisar.composedebugdrawer.demo

import android.app.Application
import com.michaelflisar.composedebugdrawer.demo.classes.DemoLogging
import com.michaelflisar.composethemer.ComposeTheme
import com.michaelflisar.composethemer.themes.ComposeThemes

class DemoApp : Application() {

    override fun onCreate() {

        super.onCreate()

        // lumberjack initialisation
        DemoLogging.init(this)

        // Themes
        ComposeTheme.register(*ComposeThemes.ALL.toTypedArray())
    }
}