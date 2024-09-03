package com.michaelflisar.composedebugdrawer.core.composables.sub

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun <T> Spinner(
    modifier: Modifier,
    expanded: MutableState<Boolean>,
    label: String,
    selected: T,
    items: List<T>,
    labelProvider: (item: T) -> String = { it.toString() },
    iconProvider: @Composable ((item: T) -> Unit)? = null,
    onItemSelected: (item: T) -> Unit
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = !expanded.value
        }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusable(false)
                .menuAnchor(),
            readOnly = true,
            enabled = true,
            value = labelProvider(selected),
            onValueChange = { },
            label = { Text(text = label) },
            leadingIcon = {
                if (iconProvider != null) {
                    iconProvider(selected)
                }
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded.value)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        if (item != selected) {
                            onItemSelected.invoke(item)
                        }
                        expanded.value = false
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (iconProvider != null) {
                                iconProvider(item)
                                Spacer(Modifier.width(8.dp))
                            }
                            Text(text = labelProvider(item))
                        }
                    }
                )
            }
        }
    }
}