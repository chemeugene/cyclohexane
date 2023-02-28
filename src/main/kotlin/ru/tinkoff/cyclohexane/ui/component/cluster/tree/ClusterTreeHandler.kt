package ru.tinkoff.cyclohexane.ui.component.cluster.tree

import mu.KLogging
import org.apache.kafka.clients.admin.ConsumerGroupDescription
import org.koin.core.component.KoinComponent
import ru.tinkoff.cyclohexane.component.kafkaclient.KafkaManager
import java.lang.Exception
import java.util.UUID

interface ClusterTreeHandler {

    suspend fun connect(cluster: ClusterTreeModel)

    fun disconnect(cluster: ClusterTreeModel)
}

class ClusterTreeHandlerImpl(
    private val kafkaManager: KafkaManager
) : ClusterTreeHandler, KoinComponent {

    override suspend fun connect(cluster: ClusterTreeModel) {
        if (cluster.connected) {
            logger.debug { "Already connected" }
            return
        }
        logger.debug { "Connecting ${cluster.name}" }
        val clusterId = cluster.id as UUID
        val topics = try {
            kafkaManager.getAccessibleTopics(clusterId)
        } catch (e: Exception) {
            logger.error(e) { "Error getting accessible topics" }
            kafkaManager.getTopics(clusterId)
        }
        val consumerGroupList = ConsumerGroupListTreeModel(parent = cluster)
        val consumerGroups = kafkaManager.getAccessibleConsumerGroups(clusterId).map {
            ConsumerGroupTreeModel(it.id, it.name, consumerGroupList) {
                kafkaManager.getConsumerGroupOffset(clusterId, it.name)
            }
        }
        cluster.consumerGroups = consumerGroupList
            .apply {
                this.consumerGroups.addAll(consumerGroups)
            }
        cluster.topics = TopicListTreeModel(parent = cluster)
            .apply {
                this.topics.addAll(
                    topics.map {
                        TopicTreeModel(
                            id = it.id,
                            name = it.name,
                            cluster
                        )
                    }.sortedBy { it.name }
                )
            }
        if (!cluster.isExpanded()) {
            cluster.toggleExpanded()
        }
        cluster.connected = true

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
            }
        }

    override fun disconnect(cluster: ClusterTreeModel) {
        logger.debug { "Disconnected ${cluster.name}" }
        cluster.disconnect()
    }

    companion object : KLogging()
}
