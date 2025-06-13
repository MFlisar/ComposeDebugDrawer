package com.michaelflisar.composedebugdrawer.plugin.kotpreferences

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerDropdown
import com.michaelflisar.kotpreferences.compose.asMutableState
import com.michaelflisar.kotpreferences.compose.asMutableStateNotNull
import com.michaelflisar.kotpreferences.core.interfaces.StorageSetting

@Composable
fun <T> DebugDrawerSettingDropdown(
    modifier: Modifier = Modifier,
    setting: StorageSetting<T>,
    items: List<T>,
    image: @Composable (() -> Unit)? = null,
    label: String = setting.getDebugLabel(),
    labelProvider: (item: T) -> String = { it.toString() }
) {
    DebugDrawerDropdown(
        modifier = modifier,
        selected = setting.asMutableStateNotNull(),
        items = items,
        image = image,
        label = label,
        labelProvider = labelProvider
    )
}