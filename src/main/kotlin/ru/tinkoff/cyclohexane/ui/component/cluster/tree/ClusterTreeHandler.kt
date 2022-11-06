package ru.tinkoff.cyclohexane.ui.component.cluster.tree

import mu.KLogging
import org.koin.core.component.KoinComponent
import ru.tinkoff.cyclohexane.component.kafkaclient.KafkaManager
import ru.tinkoff.cyclohexane.ui.component.cluster.tree.ClusterTreeModel.ItemType.TOPIC
import java.util.UUID

interface ClusterTreeHandler {

    suspend fun connect(cluster: ClusterTreeModel)

    fun disconnect(cluster: ClusterTreeModel)
}

class ClusterTreeHandlerImpl(
    private val kafkaManager: KafkaManager
) : ClusterTreeHandler, KoinComponent {

    override suspend fun connect(cluster: ClusterTreeModel) {
        logger.debug { "Connecting ${cluster.name}" }
        val topics = kafkaManager.getTopics(cluster.id as UUID)
        topics.forEach {
            cluster.children[it.name()] = ClusterTreeModel(
                id = it.topicId(),
                name = it.name(),
                type = TOPIC,
                cluster
            )
        }
        logger.debug { "Connected ${cluster.name}" }
    }

    override fun disconnect(cluster: ClusterTreeModel) {
        logger.debug { "Disconnected ${cluster.name}" }
        cluster.children.clear()
    }

    companion object : KLogging()
}
