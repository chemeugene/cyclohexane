package ru.tinkoff.cyclohexane.ui.component.cluster.tree

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.onClick
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CursorDropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.tinkoff.cyclohexane.ui.common.AppState
import ru.tinkoff.cyclohexane.ui.common.AppTheme
import ru.tinkoff.cyclohexane.ui.common.CONNECT_BTN
import ru.tinkoff.cyclohexane.ui.common.DISCONNECT_BTN
import ru.tinkoff.cyclohexane.ui.common.MainContentView.NOTHING
import ru.tinkoff.cyclohexane.ui.common.MainContentView.TOPIC_VIEW
import ru.tinkoff.cyclohexane.ui.component.AppText


object ClusterTreeView : KoinComponent {

    private val clusterTreeHandler: ClusterTreeHandler by inject()

    @Composable
    fun Panel(appState: AppState) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
            ) {
                appState.clusterTreeModel
                    .sortedBy { it.name }
                    .forEach { cluster ->
                        renderCluster(appState, cluster)
                    }
            }
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(scrollState),
                modifier = Modifier.align(Alignment.CenterEnd)
                    .fillMaxHeight()
            )
        }
    }

    @Composable
    fun renderCluster(appState: AppState, item: ClusterTreeModel) {
        TreeItem(appState, item)
        if (item.isExpanded()) {
            item.topics?.let {
                renderTopics(appState, it)
            }
            item.consumerGroups?.let {
                renderConsumerGroups(appState, it)
            }
        }
    }

    @Composable
    fun renderTopics(appState: AppState, item: TopicListTreeModel) {
        TreeItem(appState, item)
        if (item.isExpanded()) {
            item.topics.forEach { TreeItem(appState, it) }
        }
    }

    @Composable
    fun renderConsumerGroups(appState: AppState, item: ConsumerGroupListTreeModel) {
        TreeItem(appState, item)
        if (item.isExpanded()) {
            item.consumerGroups.forEach { TreeItem(appState, it) }
        }
    }

    @Composable
    @OptIn(ExperimentalFoundationApi::class)
    private fun TreeItem(
        appState: AppState,
        model: TreeModel,
        fontSize: TextUnit = 10.sp,
    ) {
        var menuVisible = remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(start = model.padding)
                .clickable {
                    appState.selectedTreeModel = model
                }
                .onClick(
                    matcher = PointerMatcher.mouse(PointerButton.Secondary),
                    onClick = {
                        appState.selectedTreeModel = model
                        menuVisible.value = true
                        model.onClickAction(appState)
                    }
                )
                .onClick(
                    matcher = PointerMatcher.mouse(PointerButton.Primary),
                    onClick = {
                        appState.selectedTreeModel = model
                        model.onClickAction(appState)
                        if (model.expandable) {
                            (model as Expandable).toggleExpanded()
                        }
                    }
                )
                .height(22.dp)
                .fillMaxWidth()
                .background(
                    if (model == appState.selectedTreeModel) {
                        AppTheme.colors.backgroundLight
                    } else {
                        AppTheme.colors.backgroundMedium
                    })
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            val active by interactionSource.collectIsHoveredAsState()

            TreeIconView(model)
            Text(
                text = model.name,
                color = if (active) LocalContentColor.current.copy(alpha = 0.60f) else LocalContentColor.current,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clipToBounds()
                    .hoverable(interactionSource),
                softWrap = true,
                fontSize = fontSize,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            model.renderMenu(appState, menuVisible)
        }
    }

    @Composable
    private fun TreeModel.renderMenu(appState: AppState, menuVisible: MutableState<Boolean>) {
        when (this) {
            is ClusterTreeModel -> {
                CursorDropdownMenu(
                    expanded = menuVisible.value,
                    onDismissRequest = { menuVisible.value = false }
                ) {
                    val coroutineScope = rememberCoroutineScope()

                    DropdownMenuItem(onClick = {
                        menuVisible.value = false
                        coroutineScope.launch(Dispatchers.IO) {
                            clusterTreeHandler.connect(appState.selectedTreeModel!! as ClusterTreeModel)
                        }
                    }) {
                        AppText(CONNECT_BTN)
                    }
                    DropdownMenuItem(onClick = {
                        menuVisible.value = false
                        clusterTreeHandler.disconnect(appState.selectedTreeModel!! as ClusterTreeModel)
                    }) {
                        AppText(DISCONNECT_BTN)
                    }
                }
            }

            is TopicTreeModel -> {

            }
        }
    }

    private fun TreeModel.onClickAction(appState: AppState) {
        when (this) {
            is ClusterTreeModel -> {
                appState.mainContentView = NOTHING
            }

            is TopicTreeModel -> {
                appState.mainContentView = TOPIC_VIEW
            }
        }
    }
}
