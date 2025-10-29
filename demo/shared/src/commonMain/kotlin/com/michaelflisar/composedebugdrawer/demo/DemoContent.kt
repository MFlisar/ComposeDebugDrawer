package com.michaelflisar.composedebugdrawer.demo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.michaelflisar.kotpreferences.compose.collectAsState
import kotlinx.coroutines.launch

@Composable
fun DemoContent(
    modifier: Modifier,
    prefs: DebugDrawerPrefs,
    open: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val enabled = prefs.enabled.collectAsState()
        val scope = rememberCoroutineScope()

        Button(
            onClick = open,
            enabled = enabled.value == true
        ) {
            Text("Open Debug Drawer")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .clickable(enabled.value != null) {
                    scope.launch { prefs.enabled.update(enabled.value != true) }
                }
                .padding(8.dp)
        ) {
            Text("Debug Drawer Enabled", modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (enabled.value == true) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                contentDescription = null
            )
        }
    }
}