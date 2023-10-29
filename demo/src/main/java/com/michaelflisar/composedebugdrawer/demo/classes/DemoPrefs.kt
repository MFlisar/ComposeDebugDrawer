package com.michaelflisar.composedebugdrawer.demo.classes

import com.michaelflisar.kotpreferences.core.SettingsModel
import com.michaelflisar.kotpreferences.datastore.DataStoreStorage

object DemoPrefs : SettingsModel(DataStoreStorage(name = "demo_prefs")) {

    enum class UIStyle {
        Style1,
        Style2,
        Style3
    }

    val expandSingleOnly by boolPref(false)

    val devBoolean1 by boolPref(true)
    val devBoolean2 by boolPref(false)
    val devStyle by enumPref(UIStyle.Style1)
    val devStyle2 by enumPref(UIStyle.Style1)
}