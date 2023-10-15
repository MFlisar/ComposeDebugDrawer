package com.michaelflisar.composedebugdrawer.core.composables

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> Spinner(
    modifier: Modifier,
    expanded: MutableState<Boolean>,
    label: String,
    selected: T,
    items: List<T>,
    labelProvider: (item: T) -> String = { it.toString() },
    iconProvider: ((item: T) -> ImageVector)? = null,
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
            items.forEachIndexed { index, item ->
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
                                Icon(
                                    imageVector = iconProvider.invoke(item),
                                    contentDescription = ""
                                )
                            }
                            Text(text = labelProvider(item))
                        }
                    }
                )
            }
        }
    }
}