package com.michaelflisar.composedebugdrawer.demo

import com.michaelflisar.composedebugdrawer.demo.classes.DemoLogging
import com.michaelflisar.toolbox.androiddemoapp.DemoApp

class DemoApp : DemoApp() {

    override fun onCreate() {

        super.onCreate()

        // lumberjack initialisation
        DemoLogging.init(this)
    }
}