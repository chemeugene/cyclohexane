package ru.tinkoff.cyclohexane.component.kafkaclient

import mu.KLogging
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.ConsumerGroupDescription
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
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

    override fun getConsumerGroups(cluster: UUID): Collection<KafkaGroup> =
        getAdminClient(cluster).listConsumerGroups().valid().get()
            .map { KafkaGroup(it.groupId(), it.groupId()) }

    override fun getAccessibleTopics(cluster: UUID): Collection<KafkaTopic> =
        ClusterEntity.find(cluster).let {
            runCatching {
                val topicBindings = getAdminClient(cluster).describeAcls(
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
                    it.pattern().name()
                }.toHashSet()
                getTopics(cluster).filter { topicBindings.contains(it.name) }
            }.onFailure {
                logger.error { "Error fetching topic list: $it" }
            }.recover {
                getTopics(cluster)
            }
        }.getOrDefault(setOf())

    override fun getAccessibleConsumerGroups(cluster: UUID): Collection<KafkaGroup> =
        ClusterEntity.find(cluster).let {
            runCatching {
                val groupBindings = getAdminClient(cluster).describeAcls(
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
                getConsumerGroups(cluster).filter { group -> groupBindings.any { group.name.contains(it) } }.toHashSet()
            }.onFailure {
                logger.error { "Error fetching consumer group list: $it" }
            }.recover {
                getConsumerGroups(cluster)
            }.getOrDefault(setOf())
        }

    override fun getConsumerGroupOffset(cluster: UUID, groupId: String): Collection<KafkaOffset> =
        getAdminClient(cluster)
            .listConsumerGroupOffsets(groupId)
            .partitionsToOffsetAndMetadata()
            .get()
            .map { KafkaOffset(it.key.topic(), it.key.partition(), it.value.offset()) }
            .sortedWith(compareBy({ it.topic }, { it.partition }))

    override fun describeConsumerGroups(
        cluster: UUID,
        groupIds: Collection<String>
    ): Map<String, ConsumerGroupDescription> =
        getAdminClient(cluster).describeConsumerGroups(groupIds).all().get()

    override fun commitOffset(cluster: UUID, groupId: String, topic: String, partition: Int, offset: Long) {
        getAdminClient(cluster)
            .alterConsumerGroupOffsets(
                groupId,
                mapOf(TopicPartition(topic, partition) to OffsetAndMetadata(offset))
            ).all().get()
    }

    private fun getAdminClient(cluster: UUID) = clients.computeIfAbsent(cluster) {
        clientFactory.createClient(ClusterEntity.find(cluster))
    }

    companion object : KLogging()
}
