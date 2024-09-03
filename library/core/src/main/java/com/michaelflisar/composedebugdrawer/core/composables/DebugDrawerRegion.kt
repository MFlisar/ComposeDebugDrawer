package com.michaelflisar.composedebugdrawer.core.composables

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedebugdrawer.core.DebugDrawerDefaults
import com.michaelflisar.composedebugdrawer.core.DebugDrawerState
import com.michaelflisar.composedebugdrawer.core.composables.sub.AnimatedVisibilityExpand

@Composable
fun DebugDrawerRegion(
    label: String,
    icon: ImageVector,
    id: String = label,
    description: String = "",
    drawerState: DebugDrawerState,
    collapsible: Boolean = true,
    itemSpacing: Dp = DebugDrawerDefaults.ITEM_SPACING,
    content: @Composable ColumnScope.() -> Unit
) {
    DebugDrawerRegion(label, id, image = { Icon(icon, null) }, description, drawerState, collapsible, itemSpacing, content)
}

@Composable
fun DebugDrawerRegion(
    label: String,
    id: String = label,
    image: @Composable (() -> Unit)? = null,
    description: String = "",
    drawerState: DebugDrawerState,
    collapsible: Boolean = true,
    itemSpacing: Dp = DebugDrawerDefaults.ITEM_SPACING,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {

        // Transition
        val transitionState =
            remember { MutableTransitionState(drawerState.isExpanded(id, collapsible)) }
        transitionState.targetState = drawerState.isExpanded(id, collapsible)
        val transition = updateTransition(transitionState, label = "transition")

        val arrowRotationDegree by transition.animateFloat(
            transitionSpec = { tween() },
            label = "arrow",
            targetValueByState = {
                if (it) -180f else 0f
            }
        )

        // Header
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                .clip(MaterialTheme.shapes.medium)
                .then(
                    if (collapsible)
                        Modifier.clickable {
                            drawerState.toggleExpanded(id)
                        }
                    else Modifier
                )
                .padding(vertical = DebugDrawerDefaults.ITEM_PADDING, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (image != null) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimary) {
                    image()
                    Spacer(Modifier.width(4.dp))
                }
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                if (description.isNotEmpty()) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            if (collapsible) {
                Icon(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(24.dp)
                        .rotate(arrowRotationDegree),
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        // Content
        AnimatedVisibilityExpand(visible = drawerState.isExpanded(id, collapsible)) {
            Column(
                modifier = Modifier.padding(all = DebugDrawerDefaults.ITEM_PADDING),
                verticalArrangement = Arrangement.spacedBy(itemSpacing)
            ) {
                content()
            }
        }
    }
}