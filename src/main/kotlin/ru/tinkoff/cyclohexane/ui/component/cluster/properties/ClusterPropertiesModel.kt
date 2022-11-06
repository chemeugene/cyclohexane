package ru.tinkoff.cyclohexane.ui.component.cluster.properties

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ru.tinkoff.cyclohexane.persistence.entity.ClusterEntity
import ru.tinkoff.cyclohexane.ui.common.SecurityType

data class ClusterPropertiesModel(
    var name: MutableState<String> = mutableStateOf(""),
    var bootstrapServers: MutableState<String> = mutableStateOf(""),
    var saslMechanism: MutableState<String> = mutableStateOf(""),
    var schemaRegistry: MutableState<String> = mutableStateOf(""),
    var jaasConfig: MutableState<String> = mutableStateOf(""),
    var securityType: MutableState<String> = mutableStateOf(SecurityType.SASL_PLAINTEXT.name)
) {
    fun toEntity(): ClusterEntity.() -> Unit = {
        name = this@ClusterPropertiesModel.name.value
        bootstrapServers = this@ClusterPropertiesModel.bootstrapServers.value
        saslMechanism = this@ClusterPropertiesModel.saslMechanism.value
        schemaRegistry = this@ClusterPropertiesModel.schemaRegistry.value
        jaasConfig = this@ClusterPropertiesModel.jaasConfig.value
        securityType = this@ClusterPropertiesModel.securityType.value
    }
}

fun ClusterEntity.toPropertiesModel() =
    ClusterPropertiesModel(
        name = mutableStateOf(this.name),
        bootstrapServers = mutableStateOf(this.bootstrapServers),
        saslMechanism = mutableStateOf(this.saslMechanism),
        schemaRegistry = mutableStateOf(this.schemaRegistry ?: ""),
        jaasConfig = mutableStateOf(this.jaasConfig ?: ""),
        securityType = mutableStateOf(this.securityType),
    )
