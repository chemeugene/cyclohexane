package ru.tinkoff.cyclohexane.database

import org.jetbrains.exposed.sql.Database
import ru.tinkoff.cyclohexane.properties.DatabaseProperties

object Database {
    val db by lazy {
        val props = DatabaseProperties()
        Database.connect(
            url = props.url,
            driver = props.driver,
            user = props.user,
            password = props.password
        )
    }
}
