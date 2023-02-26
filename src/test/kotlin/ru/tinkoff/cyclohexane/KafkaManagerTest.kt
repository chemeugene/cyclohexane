package ru.tinkoff.cyclohexane

import mu.KLogging
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import ru.tinkoff.cyclohexane.component.kafkaclient.KafkaManager
import ru.tinkoff.cyclohexane.configuration.appModule
import ru.tinkoff.cyclohexane.database.Database
import java.util.*

class KafkaManagerTest : KoinTest {

    private val kafkaManager by inject<KafkaManager>()

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(appModule)
    }

    @Test
    @Disabled("For debugging only")
    fun debugTest() {
        val qaCluster = UUID.fromString("b194e0b4-789d-44d4-9446-cfcac43e2f2b")
        Database.db
        val allTopic = kafkaManager.getTopics(qaCluster)
        val allGroup = kafkaManager.getConsumerGroups(qaCluster)
        val topics = kafkaManager.getAccessibleTopics(qaCluster)
        val consumerGroups = kafkaManager.getAccessibleConsumerGroups(qaCluster)
        logger.info { "Topics: $topics" }
        logger.info { "Topics: $consumerGroups" }
    }

    @Test
    fun happyPathTest() {
    }

    companion object : KLogging()
}