package ru.tinkoff.cyclohexane.persistence.dao.impl

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.tinkoff.cyclohexane.database.Database
import ru.tinkoff.cyclohexane.persistence.Cluster
import ru.tinkoff.cyclohexane.persistence.dao.MigrationService

class MigrationServiceImpl : MigrationService {

    override fun migrate() {
        Database.db
        transaction {
            SchemaUtils.create(Cluster)
        }
    }
}
