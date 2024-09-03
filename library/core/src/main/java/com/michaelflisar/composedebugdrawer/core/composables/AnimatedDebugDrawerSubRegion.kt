package com.michaelflisar.composedebugdrawer.core.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composedebugdrawer.core.DebugDrawerDefaults
import com.michaelflisar.composedebugdrawer.core.composables.sub.AnimatedVisibilityExpand

@Composable
fun AnimatedDebugDrawerSubRegion(
    visible: Boolean,
    itemSpacing: Dp = DebugDrawerDefaults.ITEM_SPACING,
    content: @Composable ColumnScope.() -> Unit
) {
    AnimatedVisibilityExpand(visible = visible) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = DebugDrawerDefaults.ITEM_PADDING),
            verticalArrangement = Arrangement.spacedBy(itemSpacing)
        ) {
            content()
        }
    }
}