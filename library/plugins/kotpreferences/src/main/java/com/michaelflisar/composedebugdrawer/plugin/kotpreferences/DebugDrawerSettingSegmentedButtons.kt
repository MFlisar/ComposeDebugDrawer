package com.michaelflisar.composedebugdrawer.plugin.kotpreferences

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerSegmentedButtons
import com.michaelflisar.kotpreferences.compose.asMutableState
import com.michaelflisar.kotpreferences.core.interfaces.StorageSetting

@Composable
fun <T> DebugDrawerSettingSegmentedButtons(
    modifier: Modifier = Modifier,
    setting: StorageSetting<T>,
    items: List<T>,
    image: @Composable (() -> Unit)? = null,
    labelProvider: (item: T) -> String = { setting.toString() }
) {
    DebugDrawerSegmentedButtons(
        modifier = modifier,
        selected = setting.asMutableState(),
        items = items,
        image = image,
        labelProvider = labelProvider
    )
}