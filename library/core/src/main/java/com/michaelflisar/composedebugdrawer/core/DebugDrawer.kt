package com.michaelflisar.composedebugdrawer.core

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
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

@Stable
data class DebugDrawerState(
    val drawerState: DrawerState,
    private val expandSingleOnly: Boolean = false,
    private val expandedIds: MutableState<List<String>>
) {
    fun expandedIds() = expandedIds.value

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
    drawerContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
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
                    modifier
                        .padding(end = drawerOpenMinSpaceLeft)
                        //.systemBarsPadding()
                    ,
                    //windowInsets = DrawerDefaults.windowInsets//WindowInsets(0, 0, 0, 0)
                ) {
                    CompositionLocalProvider(
                        LocalLayoutDirection provides direction
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
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