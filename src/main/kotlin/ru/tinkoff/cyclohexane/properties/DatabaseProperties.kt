package ru.tinkoff.cyclohexane.properties

data class DatabaseProperties(
    val url: String = "jdbc:h2:./cyclhexaneDb",
    val driver: String = "org.h2.Driver",
    val user: String = "sa",
    val password: String = ""
)
