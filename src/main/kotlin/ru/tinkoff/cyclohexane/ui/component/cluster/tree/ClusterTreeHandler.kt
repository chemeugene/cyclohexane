package ru.tinkoff.cyclohexane.ui.component.cluster.tree

import mu.KLogging
import org.apache.kafka.clients.admin.ConsumerGroupDescription
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
        val clusterId = cluster.id as UUID
        val topics = kafkaManager.getTopics(clusterId)
        val consumerGroups = kafkaManager.getAccessibleConsumerGroups(clusterId)
        val groupsDescription = kafkaManager.describeConsumerGroups(clusterId, consumerGroups)
        topics.forEach { topic ->
            cluster.children[topic.name()] = ClusterTreeModel(
                id = topic.topicId(),
                name = topic.name(),
                type = TOPIC,
                cluster
            ).also {
                it.consumerGroups.addAll(getTopicGroups(topic.name(), consumerGroups, groupsDescription))
            }
        }
        logger.debug { "Connected ${cluster.name}" }
    }

    private fun getTopicGroups(
        topic: String,
        groupIds: Collection<String>,
        groupsDescription: Map<String, ConsumerGroupDescription>
    ): List<String> =
        groupIds.mapNotNull { groupId ->
            groupsDescription[groupId]?.members()?.flatMap { member ->
                logger.debug { "MemberDescription $member" }
                member.assignment().topicPartitions()
            }?.filter { tp ->
                tp.topic() == topic
            }?.takeIf { it.isNotEmpty() }?.let {
                groupId
            } ?: null
        }

    override fun disconnect(cluster: ClusterTreeModel) {
        logger.debug { "Disconnected ${cluster.name}" }
        cluster.children.clear()
    }

    companion object : KLogging()
}
