package ru.tinkoff.cyclohexane.component.kafkaclient

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.ConsumerGroupDescription
import org.apache.kafka.clients.admin.TopicListing
import org.apache.kafka.common.acl.AccessControlEntryFilter
import org.apache.kafka.common.acl.AclBindingFilter
import org.apache.kafka.common.acl.AclOperation
import org.apache.kafka.common.acl.AclPermissionType
import org.apache.kafka.common.resource.PatternType
import org.apache.kafka.common.resource.ResourcePatternFilter
import org.apache.kafka.common.resource.ResourceType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.tinkoff.cyclohexane.common.parseJaasUserName
import ru.tinkoff.cyclohexane.persistence.entity.ClusterEntity
import java.util.UUID

class KafkaManagerImpl : KafkaManager, KoinComponent {

    private val clientFactory: ClientFactory by inject()

    private val clients = mutableMapOf<UUID, AdminClient>()

    override fun getTopics(cluster: UUID): Collection<KafkaTopic> =
        getAdminClient(cluster).listTopics().listings().get()
            .map { KafkaTopic(it.topicId().toString(), it.name()) }

    override fun getAccessibleTopics(cluster: UUID): Collection<KafkaTopic> =
        ClusterEntity.find(cluster).let {
            runCatching {
                getAdminClient(cluster).describeAcls(
                    AclBindingFilter(
                        ResourcePatternFilter(ResourceType.TOPIC, null, PatternType.ANY),
                        AccessControlEntryFilter(
                            "User:${parseJaasUserName(it.jaasConfig!!)}",
                            null,
                            AclOperation.DESCRIBE,
                            AclPermissionType.ALLOW
                        )
                    )
                ).values().get().map {
                    KafkaTopic(
                        id = it.pattern().name(),
                        name = it.pattern().name()
                    )
                }.toSet()
            }.getOrDefault(setOf())
        }

    override fun getAccessibleConsumerGroups(cluster: UUID): Collection<String> =
        ClusterEntity.find(cluster).let {
            runCatching {
                getAdminClient(cluster).describeAcls(
                    AclBindingFilter(
                        ResourcePatternFilter(ResourceType.GROUP, null, PatternType.ANY),
                        AccessControlEntryFilter(
                            "User:${parseJaasUserName(it.jaasConfig!!)}",
                            null,
                            AclOperation.DESCRIBE,
                            AclPermissionType.ANY
                        )
                    )
                ).values().get().map { it.pattern().name() }.toSet()
            }.getOrDefault(setOf())
        }

    override fun describeConsumerGroups(
        cluster: UUID,
        groupIds: Collection<String>
    ): Map<String, ConsumerGroupDescription> =
        getAdminClient(cluster).describeConsumerGroups(groupIds).all().get()

    private fun getAdminClient(cluster: UUID) = clients.computeIfAbsent(cluster) {
        clientFactory.createClient(ClusterEntity.find(cluster))
    }
}
