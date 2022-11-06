package ru.tinkoff.cyclohexane.ui.component.cluster.menu

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tinkoff.cyclohexane.persistence.entity.ClusterEntity
import ru.tinkoff.cyclohexane.ui.common.AppState
import ru.tinkoff.cyclohexane.ui.common.CLUSTER_ADD_BTN
import ru.tinkoff.cyclohexane.ui.common.CLUSTER_DELETE_BTN
import ru.tinkoff.cyclohexane.ui.common.CLUSTER_EDIT_BTN
import ru.tinkoff.cyclohexane.ui.common.MainContentView
import java.util.UUID

object ClusterManagementView {

    @Composable
    fun Menu(appState: AppState) {
        Row(Modifier.height(40.dp)) {
            Button(modifier = Modifier.padding(5.dp), onClick = {
                appState.selectedTreeModel = null
                appState.mainContentView = MainContentView.CLUSTER_PROPERTIES_FORM
            }) {
                Text(fontSize = 10.sp, text = CLUSTER_ADD_BTN)
            }

            Button(
                modifier = Modifier.padding(5.dp),
                enabled = appState.isClusterSelected(),
                onClick = {
                    appState.selectedTreeModel?.let {
                        appState.mainContentView = MainContentView.CLUSTER_PROPERTIES_FORM
                    }
                }) {
                Text(fontSize = 10.sp, text = CLUSTER_EDIT_BTN)
            }

            Button(
                modifier = Modifier.padding(5.dp),
                enabled = appState.isClusterSelected(),
                onClick = {
                    if (appState.isClusterSelected()) {
                        val cluster = appState.selectedTreeModel!!

                        ClusterEntity.delete(cluster.id as UUID)
                        appState.clusterTreeModel.removeIf { it.id == cluster.id }
                        appState.selectedTreeModel = null
                        appState.mainContentView = MainContentView.NOTHING
                    }
                }) {
                Text(fontSize = 10.sp, text = CLUSTER_DELETE_BTN)
            }
        }
    }
}
