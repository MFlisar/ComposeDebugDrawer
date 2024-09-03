package com.michaelflisar.composedebugdrawer.plugin.kotpreferences

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerCheckbox
import com.michaelflisar.kotpreferences.compose.collectAsStateNotNull
import com.michaelflisar.kotpreferences.core.interfaces.StorageSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DebugDrawerSettingCheckbox(
    setting: StorageSetting<Boolean>,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    foregroundTint: Color? = null,
    label: String = setting.getDebugLabel(),
    description: String = ""
) {
    DebugDrawerSettingCheckbox(setting, modifier, image = { Icon(icon, null) }, foregroundTint, label, description)
}

@Composable
fun DebugDrawerSettingCheckbox(
    setting: StorageSetting<Boolean>,
    modifier: Modifier = Modifier,
    image: @Composable (() -> Unit)? = null,
    foregroundTint: Color? = null,
    label: String = setting.getDebugLabel(),
    description: String = ""
) {
    val scope = rememberCoroutineScope()
    val checked by setting.collectAsStateNotNull()
    DebugDrawerCheckbox(
        modifier = modifier,
        image = image,
        foregroundTint = foregroundTint,
        label = label,
        description = description,
        checked = checked,
        onCheckedChange = {
            scope.launch(Dispatchers.IO) {
                setting.update(it)
            }
        }
    )
}