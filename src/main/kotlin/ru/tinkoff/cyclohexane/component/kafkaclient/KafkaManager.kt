package ru.tinkoff.cyclohexane.component.kafkaclient

import org.apache.kafka.clients.admin.TopicListing
import java.util.UUID

interface KafkaManager {

    fun getTopics(cluster: UUID): Collection<TopicListing>
}
