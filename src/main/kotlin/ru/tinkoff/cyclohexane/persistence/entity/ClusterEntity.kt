package ru.tinkoff.cyclohexane.persistence.entity

import mu.KLogging
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import ru.tinkoff.cyclohexane.database.Database
import ru.tinkoff.cyclohexane.persistence.Cluster
import java.util.UUID

class ClusterEntity(uuid: EntityID<UUID>) : UUIDEntity(uuid) {
    companion object : UUIDEntityClass<ClusterEntity>(Cluster) {

        val logger = KLogging().logger

        fun find(id: UUID): ClusterEntity =
            transaction {
                logger.debug { "Find cluster by id" }
                Database.db
                ClusterEntity.get(id)
            }

        fun create(newCluster: ClusterEntity.() -> Unit): ClusterEntity =
            transaction {
                logger.debug { "Create cluster" }
                Database.db
                ClusterEntity.new(newCluster)
            }

        fun update(clusterId: UUID, update: ClusterEntity.() -> Unit) =
            transaction {
                logger.debug { "Update cluster" }
                Database.db
                ClusterEntity.get(clusterId).apply(update)
            }

        fun delete(clusterId: UUID) =
            transaction {
                logger.debug { "Delete cluster" }
                Database.db
                ClusterEntity.get(clusterId).delete()
            }


        fun findAll(): List<ClusterEntity> {
            return transaction {
                logger.debug { "Find all clusters" }
                Database.db
                ClusterEntity.all().toList()
            }
        }
    }

    var name by Cluster.name
    var bootstrapServers by Cluster.bootstrapServers
    var saslMechanism by Cluster.saslMechanism
    var schemaRegistry by Cluster.schemaRegistry
    var jaasConfig by Cluster.jaasConfig
    var securityType by Cluster.securityType

}
