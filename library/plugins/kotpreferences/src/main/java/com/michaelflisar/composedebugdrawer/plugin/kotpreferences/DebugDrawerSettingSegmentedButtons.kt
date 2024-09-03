package com.michaelflisar.composedebugdrawer.plugin.kotpreferences

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerSegmentedButtons
import com.michaelflisar.kotpreferences.compose.collectAsStateNotNull
import com.michaelflisar.kotpreferences.core.interfaces.StorageSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun <E : Enum<E>> DebugDrawerSettingSegmentedButtons(
    modifier: Modifier = Modifier,
    setting: StorageSetting<E>,
    items: Array<E>,
    icon: ImageVector,
) {
    DebugDrawerSettingSegmentedButtons(modifier, setting, items, image = { Icon(icon, null) })
}

@Composable
fun <E : Enum<E>> DebugDrawerSettingSegmentedButtons(
    modifier: Modifier = Modifier,
    setting: StorageSetting<E>,
    items: Array<E>,
    image: @Composable (() -> Unit)? = null,
) {
    val scope = rememberCoroutineScope()
    val selected by setting.collectAsStateNotNull()
    DebugDrawerSegmentedButtons(
        modifier = modifier,
        image = image,
        selected = selected,
        items = items.toList(),
        labelProvider = { it.name }
    ) {
        scope.launch(Dispatchers.IO) {
            setting.update(it)
        }
    }
}