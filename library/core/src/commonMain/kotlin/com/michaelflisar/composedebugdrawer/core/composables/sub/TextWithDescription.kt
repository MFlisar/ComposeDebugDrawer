package com.michaelflisar.composedebugdrawer.core.composables.sub

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
internal fun TextWithDescription(
    label: String,
    description: String,
    modifier: Modifier,
    color: Color? = null
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold, //if (description.isNotEmpty()) FontWeight.Bold else FontWeight.Normal
            color = color ?: Color.Unspecified
        )
        if (description.isNotEmpty()) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = color ?: Color.Unspecified
            )
        }
    }
}