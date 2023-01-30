package ru.tinkoff.cyclohexane.common

private val JAAS_USERNAME = ".*username\\s*=\\s*\"([^\"]*)\"".toRegex()
fun parseJaasUserName(jaas: String): String =
    JAAS_USERNAME.find(jaas)?.groupValues?.get(1)!!
