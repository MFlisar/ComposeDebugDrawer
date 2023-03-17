package com.michaelflisar.composedebugdrawer.demo

import android.app.Application
import com.michaelflisar.composedebugdrawer.demo.classes.DemoLogging

class DemoApp : Application() {

    override fun onCreate() {

        super.onCreate()

        // lumberjack initialisation
        DemoLogging.init(this)
    }
}