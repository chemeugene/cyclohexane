package ru.tinkoff.cyclohexane.component.kafkaclient

import org.apache.kafka.clients.admin.ConsumerGroupDescription
import java.util.UUID

interface KafkaManager {

    fun getTopics(cluster: UUID): Collection<KafkaTopic>

    fun getConsumerGroups(cluster: UUID): Collection<KafkaGroup>

    fun getAccessibleTopics(cluster: UUID): Collection<KafkaTopic>

    fun getAccessibleConsumerGroups(cluster: UUID): Collection<KafkaGroup>

    fun getConsumerGroupOffset(cluster: UUID, groupId: String): Collection<KafkaOffset>

    fun describeConsumerGroups(
        cluster: UUID,
        groupIds: Collection<String>
    ): Map<String, ConsumerGroupDescription>

    fun commitOffset(
        cluster: UUID,
        groupId: String,
        topic: String,
        partition: Int,
        offset: Long
    )

}

data class KafkaTopic(
    val id: String,
    val name: String
)

data class KafkaGroup(
    val id: String,
    val name: String
)

data class KafkaOffset(
    val topic: String,
    val partition: Int,
    var offset: Long
)
