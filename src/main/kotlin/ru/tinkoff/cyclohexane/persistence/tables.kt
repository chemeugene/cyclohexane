package ru.tinkoff.cyclohexane.persistence

import org.jetbrains.exposed.dao.id.UUIDTable

object Cluster : UUIDTable() {
    val name = varchar("name", length = 255)
    val bootstrapServers = text("bootstrapServers")
    val saslMechanism = varchar("saslMechanism", length = 255)
    val schemaRegistry = varchar("schemaRegistry", length = 255).nullable()
    val jaasConfig = text("jaasConfig").nullable()
    val securityType = varchar("securityType", length = 255)
}
