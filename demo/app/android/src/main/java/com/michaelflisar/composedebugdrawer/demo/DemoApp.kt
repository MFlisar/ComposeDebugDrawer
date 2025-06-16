package com.michaelflisar.composedebugdrawer.demo

import android.app.Application
import com.michaelflisar.composedebugdrawer.demo.classes.DemoLogging
import com.michaelflisar.kotpreferences.storage.datastore.DataStoreStorage
import com.michaelflisar.kotpreferences.storage.datastore.create

class DemoApp : Application() {

    companion object {
        lateinit var PREFS: DebugDrawerPrefs
            private set
    }

    override fun onCreate() {

        super.onCreate()

        PREFS = DebugDrawerPrefs(
            DataStoreStorage.create("debug_drawer_prefs")
        )

        // lumberjack initialisation
        DemoLogging.init(this)
    }
}