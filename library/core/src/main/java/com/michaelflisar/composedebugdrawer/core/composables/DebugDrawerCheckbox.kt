package com.michaelflisar.composedebugdrawer.core.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedebugdrawer.core.composables.sub.TextWithDescription

@Composable
fun DebugDrawerCheckbox(
    label: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    description: String = "",
    foregroundTint: Color? = null,
    onCheckedChange: (checked: Boolean) -> Unit
) {
    DebugDrawerCheckbox(label, checked, modifier, image = { Icon(icon, null) }, description, foregroundTint, onCheckedChange)
}

@Composable
fun DebugDrawerCheckbox(
    label: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    image: @Composable (() -> Unit)? = null,
    description: String = "",
    foregroundTint: Color? = null,
    onCheckedChange: (checked: Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (image != null) {
            image()
            Spacer(Modifier.width(4.dp))
        }
        TextWithDescription(
            modifier = Modifier.weight(1f),
            label = label, description = description, color = foregroundTint
        )

        // Checkbox should not be larger than icons to save some space...
        Checkbox(
            modifier = Modifier.height(24.dp),
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}