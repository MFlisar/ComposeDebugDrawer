package com.michaelflisar.composedebugdrawer.demo

import com.michaelflisar.kotpreferences.core.SettingsModel
import com.michaelflisar.kotpreferences.core.interfaces.Storage

class DebugDrawerPrefs(
    storage: Storage
) : SettingsModel(storage) {

    enum class UIStyle {
        Style1,
        Style2,
        Style3
    }

    val expandSingleOnly by boolPref(false)

    val devBoolean1 by boolPref(true)
    val devBoolean2 by boolPref(false)
    val devStyle by enumPref(UIStyle.Style1, UIStyle.entries)
    val devStyle2 by enumPref(UIStyle.Style1, UIStyle.entries)
}