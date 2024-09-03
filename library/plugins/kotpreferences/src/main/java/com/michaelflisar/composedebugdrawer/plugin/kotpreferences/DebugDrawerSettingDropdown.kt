package com.michaelflisar.composedebugdrawer.plugin.kotpreferences

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerDropdown
import com.michaelflisar.kotpreferences.compose.collectAsStateNotNull
import com.michaelflisar.kotpreferences.core.interfaces.StorageSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun <E : Enum<E>> DebugDrawerSettingDropdown(
    modifier: Modifier = Modifier,
    setting: StorageSetting<E>,
    items: Array<E>,
    icon: ImageVector,
    label: String = setting.getDebugLabel()
) {
    DebugDrawerSettingDropdown(modifier, setting, items, image = { Icon(icon, null) }, label)
}

@Composable
fun <E : Enum<E>> DebugDrawerSettingDropdown(
    modifier: Modifier = Modifier,
    setting: StorageSetting<E>,
    items: Array<E>,
    image: @Composable (() -> Unit)? = null,
    label: String = setting.getDebugLabel()
) {
    val scope = rememberCoroutineScope()
    val selected by setting.collectAsStateNotNull()

    DebugDrawerDropdown(
        modifier = modifier,
        image = image,
        label = label,
        selected = selected,
        items = items.toList(),
        labelProvider = { it.name },
        iconProvider = null
    ) {
        scope.launch(Dispatchers.IO) {
            setting.update(it)
        }
    }
}