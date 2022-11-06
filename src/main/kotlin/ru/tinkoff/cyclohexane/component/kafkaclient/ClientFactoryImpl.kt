package ru.tinkoff.cyclohexane.component.kafkaclient

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.common.config.SaslConfigs
import ru.tinkoff.cyclohexane.persistence.entity.ClusterEntity
import java.util.Properties

class ClientFactoryImpl : ClientFactory {

    override fun createClient(cluster: ClusterEntity): AdminClient =
        AdminClient.create(Properties().apply {
            put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, cluster.bootstrapServers)
            put(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, cluster.securityType)
            put(SaslConfigs.SASL_MECHANISM, cluster.saslMechanism)
            put(SaslConfigs.SASL_JAAS_CONFIG, cluster.jaasConfig)
        })
}
