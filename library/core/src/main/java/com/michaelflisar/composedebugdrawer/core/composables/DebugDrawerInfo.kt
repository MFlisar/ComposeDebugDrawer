package com.michaelflisar.composedebugdrawer.core.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedebugdrawer.core.DebugDrawerDefaults

@Composable
fun DebugDrawerInfo(
    title: String,
    info: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.weight(DebugDrawerDefaults.TITLE_TO_TEXT_RATIO),
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.weight(1f - DebugDrawerDefaults.TITLE_TO_TEXT_RATIO),
            text = info,
            style = MaterialTheme.typography.bodySmall
        )
    }
}