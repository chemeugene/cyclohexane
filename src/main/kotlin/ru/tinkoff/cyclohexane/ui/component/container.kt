package ru.tinkoff.cyclohexane.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.tinkoff.cyclohexane.ui.common.AppState
import ru.tinkoff.cyclohexane.ui.component.cluster.menu.ClusterManagementView
import ru.tinkoff.cyclohexane.ui.component.cluster.properties.ClusterPropertiesView
import ru.tinkoff.cyclohexane.ui.component.cluster.tree.ClusterTreeView
import ru.tinkoff.cyclohexane.ui.component.topic.TopicMainView

@Composable
fun MainContainer() {
    val appState = remember { AppState }
    Row(Modifier.fillMaxSize()) {
        Column(Modifier.padding(10.dp)
            .fillMaxWidth(fraction = 0.27f)
            .fillMaxHeight()
        ) {
            ClusterManagementView.Menu(appState)
            ClusterTreeView.Panel(appState)
        }
        Column(Modifier.fillMaxWidth()) {
            ClusterPropertiesView.Form(appState)
            TopicMainView.Panel(appState)
        }
    }
}
