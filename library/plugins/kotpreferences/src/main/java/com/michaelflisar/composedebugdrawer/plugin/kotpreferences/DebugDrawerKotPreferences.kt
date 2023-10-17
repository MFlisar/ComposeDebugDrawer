package com.michaelflisar.composedebugdrawer.plugin.kotpreferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.michaelflisar.composedebugdrawer.core.DebugDrawerCheckbox
import com.michaelflisar.composedebugdrawer.core.DebugDrawerDropdown
import com.michaelflisar.composedebugdrawer.core.DebugDrawerSegmentedButtons
import com.michaelflisar.kotpreferences.compose.collectAsStateNotNull
import com.michaelflisar.kotpreferences.core.interfaces.StorageSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@Composable
fun DebugDrawerSettingCheckbox(
    modifier: Modifier = Modifier,
    setting: StorageSetting<Boolean>,
    icon: ImageVector? = null,
    foregroundTint: Color? = null,
    label: String = setting.getDebugLabel(),
    description: String = ""
) {
    val scope = rememberCoroutineScope()
    val checked by setting.collectAsStateNotNull()
    DebugDrawerCheckbox(
        modifier = modifier,
        icon = icon,
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

@Composable
fun <E : Enum<E>> DebugDrawerSettingDropdown(
    modifier: Modifier = Modifier,
    setting: StorageSetting<E>,
    items: Array<E>,
    icon: ImageVector? = null,
    label: String = setting.getDebugLabel()
) {
    val scope = rememberCoroutineScope()
    val selected by setting.collectAsStateNotNull()

    DebugDrawerDropdown(
        modifier = modifier,
        icon = icon,
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

@Composable
fun <E : Enum<E>> DebugDrawerSettingSegmentedButtons(
    modifier: Modifier = Modifier,
    setting: StorageSetting<E>,
    items: Array<E>,
    icon: ImageVector? = null
) {
    val scope = rememberCoroutineScope()
    val selected by setting.collectAsStateNotNull()
    DebugDrawerSegmentedButtons(
        modifier = modifier,
        icon = icon,
        selected = selected,
        items = items.toList(),
        labelProvider = { it.name }
    ) {
        scope.launch(Dispatchers.IO) {
            setting.update(it)
        }
    }
}

fun <T> StorageSetting<T>.getDebugLabel(): String {
    return key.let {
        var trimmed = it.replaceFirstChar { it.uppercase() }
        val stringToRemove = emptyList<String>()//listOf("debug", "tmp", "temp")
        stringToRemove.forEach {
            if (trimmed.startsWith(it, true))
                trimmed = trimmed.substring(it.length)
        }
        val words = trimmed.split(Pattern.compile("(?=\\p{Lu})"))
        // einzelne nun aufgesplittete Großbuchstaben wieder zusammenfassen
        // bspw. "NY" in "debugTestNYCity"
        val words2 = mutableListOf<String>()
        var tmp = ""
        words.forEach {
            if (it.length == 1 && it == it.uppercase()) {
                tmp += it
            } else {
                if (tmp.isNotEmpty()) {
                    words2.add(tmp)
                    tmp = ""
                }
                words2.add(it)
            }
        }
        if (tmp.isNotEmpty()) {
            words2.add(tmp)
            tmp = ""
        }
        //L.d { "trimmed = $trimmed | words = ${words.size} (${words.joinToString(";")})" }
        //L.d { "trimmed = $trimmed | words2 = ${words2.size} (${words2.joinToString(";")})" }
        words2.joinToString(" ")
    }
}