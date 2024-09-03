package com.michaelflisar.composedebugdrawer.core.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedebugdrawer.core.composables.sub.SegmentedButtons

@Composable
fun <T> DebugDrawerSegmentedButtons(
    items: List<T>,
    selected: MutableState<T>,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    labelProvider: (item: T) -> String = { it.toString() }
) {
    DebugDrawerSegmentedButtons(items, selected, modifier, image = { Icon(icon, null) }, labelProvider)
}

@Composable
fun <T> DebugDrawerSegmentedButtons(
    items: List<T>,
    selected: MutableState<T>,
    modifier: Modifier = Modifier,
    image: @Composable (() -> Unit)? = null,
    labelProvider: (item: T) -> String = { it.toString() }
) {
    DebugDrawerSegmentedButtons(
        items,
        selected.value,
        modifier,
        image,
        labelProvider
    ) {
        selected.value = it
    }
}

@Composable
fun <T> DebugDrawerSegmentedButtons(
    items: List<T>,
    selected: T,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    labelProvider: (item: T) -> String = { it.toString() },
    onItemSelected: (item: T) -> Unit
) {
    DebugDrawerSegmentedButtons(items, selected, modifier, image = { Icon(icon, null) }, labelProvider, onItemSelected)
}

@Composable
fun <T> DebugDrawerSegmentedButtons(
    items: List<T>,
    selected: T,
    modifier: Modifier = Modifier,
    image: @Composable (() -> Unit)? = null,
    labelProvider: (item: T) -> String = { it.toString() },
    onItemSelected: (item: T) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (image != null) {
            image()
            Spacer(Modifier.width(4.dp))
        }
        val index by remember(selected) {
            derivedStateOf { items.indexOf(selected) }
        }
        SegmentedButtons(
            modifier = Modifier.weight(1f),
            items = items.map { labelProvider(it) },
            selectedIndex = index,
            onItemSelected = { onItemSelected(items[it]) }
        )
    }
}