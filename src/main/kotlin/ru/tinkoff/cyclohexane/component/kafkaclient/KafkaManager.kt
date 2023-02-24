package ru.tinkoff.cyclohexane.component.kafkaclient

import org.apache.kafka.clients.admin.ConsumerGroupDescription
import org.apache.kafka.clients.admin.TopicListing
import java.util.UUID

interface KafkaManager {

    fun getTopics(cluster: UUID): Collection<KafkaTopic>

    fun getAccessibleTopics(cluster: UUID): Collection<KafkaTopic>

    fun getAccessibleConsumerGroups(cluster: UUID): Collection<String>

    fun describeConsumerGroups(
        cluster: UUID,
        groupIds: Collection<String>
    ): Map<String, ConsumerGroupDescription>

}

data class KafkaTopic(
    val id: String,
    val name: String
)
