package com.michaelflisar.composedebugdrawer.plugin.kotpreferences

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerCheckbox
import com.michaelflisar.kotpreferences.compose.asMutableStateNotNull
import com.michaelflisar.kotpreferences.core.interfaces.StorageSetting

@Composable
fun DebugDrawerSettingCheckbox(
    setting: StorageSetting<Boolean>,
    modifier: Modifier = Modifier,
    image: @Composable (() -> Unit)? = null,
    foregroundTint: Color? = null,
    label: String = setting.getDebugLabel(),
    description: String = ""
) {
    DebugDrawerCheckbox(
        checked = setting.asMutableStateNotNull(),
        modifier = modifier,
        image = image,
        foregroundTint = foregroundTint,
        label = label,
        description = description,
    )
}