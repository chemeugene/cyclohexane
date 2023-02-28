package ru.tinkoff.cyclohexane.ui.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.tinkoff.cyclohexane.ui.common.AppState
import ru.tinkoff.cyclohexane.ui.common.PanelState
import ru.tinkoff.cyclohexane.ui.common.ResizablePanel
import ru.tinkoff.cyclohexane.ui.common.VerticalSplittable
import ru.tinkoff.cyclohexane.ui.component.cluster.menu.ClusterManagementView
import ru.tinkoff.cyclohexane.ui.component.cluster.properties.ClusterPropertiesView
import ru.tinkoff.cyclohexane.ui.component.cluster.tree.ClusterTreeView

@Composable
fun MainContainer() {
    val appState = remember { AppState }
    val panelState = remember { PanelState() }

    val animatedSize = if (panelState.splitter.isResizing) {
        if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize
    } else {
        animateDpAsState(
            if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize,
            SpringSpec(stiffness = Spring.StiffnessLow)
        ).value
    }

    VerticalSplittable(
        Modifier.fillMaxSize(),
        panelState.splitter,
        onResize = {
            panelState.expandedSize =
                (panelState.expandedSize + it).coerceAtLeast(panelState.expandedSizeMin)
        }
    ) {
        ResizablePanel(Modifier.width(animatedSize).fillMaxHeight(), panelState) {
            Column(Modifier.padding(10.dp)
                .fillMaxSize()
            ) {
                ClusterManagementView.Menu(appState)
                ClusterTreeView.Panel(appState)
            }
        }
        Column(Modifier.fillMaxWidth()) {
            ClusterPropertiesView.Form(appState)
            MainView.Panel(appState)
        }
    }
}
