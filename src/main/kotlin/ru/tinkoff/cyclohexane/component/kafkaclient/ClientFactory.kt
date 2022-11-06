package ru.tinkoff.cyclohexane.component.kafkaclient

import org.apache.kafka.clients.admin.AdminClient
import ru.tinkoff.cyclohexane.persistence.entity.ClusterEntity

interface ClientFactory {

    fun createClient(cluster: ClusterEntity): AdminClient
}
