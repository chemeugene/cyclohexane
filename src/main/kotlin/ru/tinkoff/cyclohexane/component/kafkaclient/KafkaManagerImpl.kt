package ru.tinkoff.cyclohexane.component.kafkaclient

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.TopicListing
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.tinkoff.cyclohexane.persistence.entity.ClusterEntity
import java.util.UUID

class KafkaManagerImpl : KafkaManager, KoinComponent {

    private val clientFactory: ClientFactory by inject()

    private val clients = mutableMapOf<UUID, AdminClient>()

    override fun getTopics(id: UUID): Collection<TopicListing> {
        val result = clients.computeIfAbsent(id) {
            clientFactory.createClient(ClusterEntity.find(id))
        }.listTopics()
        return result.listings().get()
    }
}
