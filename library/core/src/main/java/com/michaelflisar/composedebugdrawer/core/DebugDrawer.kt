package com.michaelflisar.composedebugdrawer.core

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedebugdrawer.core.composables.SegmentedButtons
import com.michaelflisar.composedebugdrawer.core.composables.Spinner
import kotlinx.coroutines.launch

// -------------
// DEFAULTS
// -------------

object DebugDrawerDefaults {
    val ITEM_SPACING = 8.dp
    val ITEM_PADDING = 8.dp
    val DEFAULT_MIN_SPACE_LEFT = 48.dp
    const val TITLE_TO_TEXT_RATIO = 1f / 2f
    const val EMPTY = "--"
    const val TRUE = "TRUE"
    const val FALSE = "FALSE"
}

// -------------
// Drawer
// -------------

@OptIn(ExperimentalMaterial3Api::class)
@Stable
data class DebugDrawerState(
    val drawerState: DrawerState,
    private val expandSingleOnly: Boolean = false,
    private val expandedIds: MutableState<List<String>>
) {
    fun toggleExpanded(id: String) {
        if (expandedIds.value.contains(id)) {
            expandedIds.value = expandedIds.value - id
        } else {
            if (expandSingleOnly)
                expandedIds.value = listOf(id)
            else
                expandedIds.value = expandedIds.value + id
        }
    }

    fun collapseAll() {
        expandedIds.value = listOf()
    }

    fun collapse(vararg ids: String) {
        expandedIds.value = expandedIds.value - ids.toSet()
    }

    fun expand(vararg ids: String) {
        expandedIds.value = ids.toList()
    }

    fun isExpanded(id: String, collapsible: Boolean) =
        !collapsible || expandedIds.value.contains(id)
}

@Composable
fun rememberDebugDrawerState(
    initialValue: DrawerValue = DrawerValue.Closed,
    expandSingleOnly: Boolean = false,
    confirmStateChange: (DrawerValue) -> Boolean = { true },
    initialExpandedIds: List<String> = emptyList()
): DebugDrawerState {
    val drawerState = rememberSaveable(
        initialValue,
        confirmStateChange,
        saver = DrawerState.Saver(confirmStateChange)
    ) {
        DrawerState(initialValue, confirmStateChange)
    }
    val expandedIds = rememberSaveable { mutableStateOf(initialExpandedIds) }
    LaunchedEffect(expandSingleOnly) {
        if (expandSingleOnly && expandedIds.value.size > 1) {
            expandedIds.value = emptyList()
        }
    }
    return DebugDrawerState(drawerState, expandSingleOnly, expandedIds)
}

@Composable
fun DebugDrawer(
    enabled: Boolean = true,
    drawerContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    drawerState: DebugDrawerState = rememberDebugDrawerState(DrawerValue.Closed),
    gesturesEnabled: Boolean = true,
    scrimColor: Color = DrawerDefaults.scrimColor,
    drawerOpenMinSpaceLeft: Dp = DebugDrawerDefaults.DEFAULT_MIN_SPACE_LEFT,
    drawerItemSpacing: Dp = DebugDrawerDefaults.ITEM_SPACING,
    drawerContentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    content: @Composable () -> Unit
) {
    if (!enabled) {
        content()
        return
    }

    val scope = rememberCoroutineScope()
    BackHandler(enabled = drawerState.drawerState.isOpen) {
        scope.launch {
            drawerState.drawerState.close()
        }
    }

    val direction = LocalLayoutDirection.current
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet(
                    modifier.padding(end = drawerOpenMinSpaceLeft)
                ) {
                    CompositionLocalProvider(
                        LocalLayoutDirection provides direction
                    ) {
                        Column(
                            modifier = Modifier
                                .sizeIn(
                                    minWidth = 240.dp,//MinimumDrawerWidth,
                                    maxWidth = DrawerDefaults.MaximumDrawerWidth
                                )
                                .padding(drawerContentPadding)
                                .verticalScroll(rememberScrollState())
                                .animateContentSize(),
                            verticalArrangement = Arrangement.spacedBy(drawerItemSpacing)
                        ) {
                            drawerContent()
                        }
                    }
                }
            },
            modifier = modifier,
            drawerState = drawerState.drawerState,
            content = {
                CompositionLocalProvider(
                    LocalLayoutDirection provides direction
                ) {
                    content()
                }
            },
            gesturesEnabled = gesturesEnabled,
            scrimColor = scrimColor
        )
    }
}

// -------------
// Content Items
// -------------

@Composable
fun DebugDrawerRegion(
    icon: ImageVector? = null,
    label: String,
    id: String = label,
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
            if (icon != null) {
                Icon(
                    modifier = Modifier.padding(end = 4.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
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

@Composable
fun DebugDrawerButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    icon: ImageVector? = null,
    foregroundTint: Color? = null,
    label: String,
    outline: Boolean = true,
    onClick: () -> Unit
) {
    if (outline) {
        OutlinedButton(
            modifier = modifier,
            onClick = onClick
        ) {
            if (icon != null) {
                Icon(
                    modifier = Modifier.padding(end = 4.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = foregroundTint ?: LocalContentColor.current
                )
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
            if (icon != null) {
                Icon(
                    modifier = Modifier.padding(end = 4.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = foregroundTint ?: LocalContentColor.current
                )
            }
            Text(text = label, color = foregroundTint ?: Color.Unspecified)
        }
    }
}

@Composable
fun DebugDrawerCheckbox(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    foregroundTint: Color? = null,
    label: String,
    description: String = "",
    checked: Boolean,
    onCheckedChange: (checked: Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                imageVector = icon,
                contentDescription = null,
                tint = foregroundTint ?: LocalContentColor.current
            )
        }
        TextWithDescription(modifier = Modifier.weight(1f), label, description, foregroundTint)

        // Checkbox should not be larger than icons to save some space...
        Checkbox(
            modifier = Modifier.height(24.dp),
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun <T> DebugDrawerDropdown(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    label: String,
    selected: T,
    items: List<T>,
    labelProvider: (item: T) -> String = { it.toString() },
    iconProvider: ((item: T) -> ImageVector)? = null,
    onItemSelected: (item: T) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                imageVector = icon,
                contentDescription = null
            )
        }
        val expanded = remember { mutableStateOf(false) }
        Spinner(
            modifier = Modifier.weight(1f),
            expanded = expanded,
            label = label,
            selected = selected,
            items = items,
            labelProvider = labelProvider,
            iconProvider = iconProvider,
            onItemSelected = onItemSelected
        )
    }
}

@Composable
fun <T> DebugDrawerSegmentedButtons(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    selected: MutableState<T>,
    items: List<T>,
    labelProvider: (item: T) -> String = { it.toString() }
) {
    DebugDrawerSegmentedButtons(modifier, icon, selected.value, items, labelProvider) {
        selected.value = it
    }
}

@Composable
fun <T> DebugDrawerSegmentedButtons(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    selected: T,
    items: List<T>,
    labelProvider: (item: T) -> String = { it.toString() },
    onItemSelected: (item: T) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                imageVector = icon,
                contentDescription = null
            )
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


@Composable
fun DebugDrawerInfo(
    modifier: Modifier = Modifier,
    title: String,
    info: String
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

@Composable
fun DebugDrawerDivider(
    modifier: Modifier = Modifier.fillMaxWidth(),
    info: String
) {
    if (info.isEmpty()) {
        Divider(
            modifier = modifier,
            color = MaterialTheme.colorScheme.outline
        )
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                text = info,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                //color = MaterialTheme.colorScheme.primary
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

// ----------------
// Helper functions
// ----------------

@Composable
private fun TextWithDescription(
    modifier: Modifier,
    label: String,
    description: String,
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

@Composable
private fun AnimatedVisibilityExpand(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(
            initialAlpha = 0.3f
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            // Expand from the top.
            shrinkTowards = Alignment.Top,
            animationSpec = tween()
        ) + fadeOut(
            // Fade in with the initial alpha of 0.3f.
            animationSpec = tween()
        )
    }

    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = enterTransition,
        exit = exitTransition
    ) {
        content()
    }
}