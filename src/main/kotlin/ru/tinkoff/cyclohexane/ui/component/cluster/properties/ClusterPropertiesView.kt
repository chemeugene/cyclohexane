package ru.tinkoff.cyclohexane.ui.component.cluster.properties

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tinkoff.cyclohexane.persistence.entity.ClusterEntity
import ru.tinkoff.cyclohexane.ui.common.AppState
import ru.tinkoff.cyclohexane.ui.common.BOOTSTRAP_SERVERS
import ru.tinkoff.cyclohexane.ui.common.CLUSTER_NAME
import ru.tinkoff.cyclohexane.ui.common.CLUSTER_SAVE_BTN
import ru.tinkoff.cyclohexane.ui.common.JAAS_CONFIG
import ru.tinkoff.cyclohexane.ui.common.MainContentView.CLUSTER_PROPERTIES_FORM
import ru.tinkoff.cyclohexane.ui.common.MainContentView.NOTHING
import ru.tinkoff.cyclohexane.ui.common.SASL_MECHANISM
import ru.tinkoff.cyclohexane.ui.common.SCHEMA_REGISTRY
import ru.tinkoff.cyclohexane.ui.common.SECURITY_TYPE_DN
import ru.tinkoff.cyclohexane.ui.common.SecurityType
import ru.tinkoff.cyclohexane.ui.component.AppText
import ru.tinkoff.cyclohexane.ui.component.cluster.tree.toTreeModel
import java.util.UUID

object ClusterPropertiesView {

    @Composable
    private fun AppTextField(
        label: String, value: String = "",
        onValueChange: (String) -> Unit
    ) =
        TextField(
            modifier = Modifier.padding(10.dp).fillMaxWidth().height(50.dp),
            label = { AppText(label) },
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 10.sp)
        )

    @Composable
    fun Form(appState: AppState) {
        if (appState.mainContentView == CLUSTER_PROPERTIES_FORM) {
            var expanded by remember { mutableStateOf(false) }
            var cluster = if (!appState.isClusterSelected()) {
                remember { ClusterPropertiesModel() }!!
            } else {
                remember(appState.selectedTreeModel!!.id) {
                    ClusterEntity.find(appState.selectedTreeModel!!.id as UUID).toPropertiesModel()
                }!!
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                AppTextField(CLUSTER_NAME, cluster.name.value) {
                    cluster.name.value = it
                }
                AppTextField(BOOTSTRAP_SERVERS, cluster.bootstrapServers.value) {
                    cluster.bootstrapServers.value = it
                }
                AppTextField(SASL_MECHANISM, cluster.saslMechanism.value) {
                    cluster.saslMechanism.value = it
                }
                AppTextField(SCHEMA_REGISTRY, cluster.schemaRegistry.value) {
                    cluster.schemaRegistry.value = it
                }
                AppTextField(JAAS_CONFIG, cluster.jaasConfig.value) {
                    cluster.jaasConfig.value = it
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(modifier = Modifier.padding(5.dp), text = SECURITY_TYPE_DN)
                    Button(onClick = { expanded = true }) {
                        Text(cluster.securityType.value)
                    }
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    SecurityType.values().forEach {
                        DropdownMenuItem(onClick = {
                            cluster.securityType.value = it.name
                            expanded = false
                        }) {
                            AppText(it.name)
                        }
                    }
                }
                Button(modifier = Modifier.padding(5.dp), onClick = {
                    if (!appState.isClusterSelected()) {
                        val newCluster = ClusterEntity.create(cluster.toEntity())
                        appState.clusterTreeModel.add(newCluster.toTreeModel())
                        appState.mainContentView = NOTHING
                    } else {
                        val updated = ClusterEntity.update(appState.selectedTreeModel!!.id as UUID, cluster.toEntity())
                        appState.clusterTreeModel.removeIf { it.id == appState.selectedTreeModel!!.id }
                        appState.clusterTreeModel.add(updated.toTreeModel())
                        appState.mainContentView = NOTHING
                    }
                }) {
                    Text(fontSize = 10.sp, text = CLUSTER_SAVE_BTN)
                }
            }
        }
    }
}
