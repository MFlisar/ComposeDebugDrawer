package com.michaelflisar.demo

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.michaelflisar.kotpreferences.storage.keyvalue.LocalStorageKeyValueStorage

val storage = LocalStorageKeyValueStorage.create(key = "prefs")
val prefs = DebugDrawerPrefs(storage)

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
suspend fun main() {

    ComposeViewport(
        // mit container id geht es nicht --> wäre aber gut, dann würde ein Loader angezeigt werden, aktuell wird der nicht angezeigt...
        // viewportContainerId = wasmSetup.canvasElementId
    ) {
        DemoApp(
            prefs = prefs,
            fileLoggingSetup = null,
            isDebugBuild = true
        )
    }
}