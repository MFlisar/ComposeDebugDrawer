package com.michaelflisar.composedebugdrawer.core.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedebugdrawer.core.composables.sub.Spinner

@Composable
fun <T> DebugDrawerDropdown(
    label: String,
    items: List<T>,
    selected: T,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    labelProvider: (item: T) -> String = { it.toString() },
    iconProvider: @Composable ((item: T) -> Unit)? = null,
    onItemSelected: (item: T) -> Unit
) {
    DebugDrawerDropdown(
        label = label,
        items = items,
        selected = selected,
        modifier = modifier,
        image = { Icon(icon, null) },
        labelProvider = labelProvider,
        iconProvider = iconProvider,
        onItemSelected = onItemSelected
    )
}

@Composable
fun <T> DebugDrawerDropdown(
    label: String,
    items: List<T>,
    selected: T,
    modifier: Modifier = Modifier,
    image: @Composable (() -> Unit)? = null,
    labelProvider: (item: T) -> String = { it.toString() },
    iconProvider: @Composable ((item: T) -> Unit)? = null,
    onItemSelected: (item: T) -> Unit
) {
    val selected = items.indexOf(selected)
    val labelProvider = { item: T, index: Int -> labelProvider(item) }
    val iconProvider: @Composable ((item: T, index: Int) -> Unit)? = if (iconProvider != null) {
        { item: T, index: Int -> iconProvider(item) }
    } else null
    val onItemSelected = { item: T, index: Int -> onItemSelected(item) }
    DebugDrawerDropdownImpl(
        label,
        items,
        selected,
        modifier,
        image,
        labelProvider,
        iconProvider,
        onItemSelected
    )
}

@Composable
private fun <T> DebugDrawerDropdownImpl(
    label: String,
    items: List<T>,
    selected: Int,
    modifier: Modifier = Modifier,
    image: @Composable (() -> Unit)? = null,
    labelProvider: (item: T, index: Int) -> String = { item, index -> item.toString() },
    iconProvider: @Composable ((item: T, index: Int) -> Unit)? = null,
    onItemSelected: (item: T, index: Int) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (image != null) {
            image()
            Spacer(Modifier.width(4.dp))
        }
        val expanded = remember { mutableStateOf(false) }
        Spinner(
            modifier = Modifier.weight(1f),
            expanded = expanded,
            label = label,
            selected = selected,
            items = items,
            labelProvider = labelProvider,
            iconProvider = iconProvider,
            onItemSelected = onItemSelected
        )
    }
}