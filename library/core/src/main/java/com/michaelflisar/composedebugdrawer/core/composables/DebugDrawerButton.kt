package com.michaelflisar.composedebugdrawer.core.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun DebugDrawerButton(
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    icon: ImageVector,
    foregroundTint: Color? = null,
    outline: Boolean = true,
    onClick: () -> Unit
) {
    DebugDrawerButton(label, modifier, image = { Icon(icon, null) }, foregroundTint, outline, onClick)
}

@Composable
fun DebugDrawerButton(
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    image: @Composable (() -> Unit)? = null,
    foregroundTint: Color? = null,
    outline: Boolean = true,
    onClick: () -> Unit
) {
    if (outline) {
        OutlinedButton(
            modifier = modifier,
            onClick = onClick
        ) {
            if (image != null) {
                CompositionLocalProvider(LocalContentColor provides (foregroundTint ?: LocalContentColor.current)) {
                    image()
                    Spacer(Modifier.width(4.dp))
                }
            }
            Text(text = label, color = foregroundTint ?: Color.Unspecified)
        }
    } else {
        Button(
            modifier = modifier,
            onClick = onClick,
            colors = foregroundTint?.let { ButtonDefaults.buttonColors(contentColor = foregroundTint) }
                ?: ButtonDefaults.buttonColors()
        ) {
            if (image != null) {
                CompositionLocalProvider(LocalContentColor provides (foregroundTint ?: LocalContentColor.current)) {
                    image()
                    Spacer(Modifier.width(4.dp))
                }
            }
            Text(text = label, color = foregroundTint ?: Color.Unspecified)
        }
    }
}