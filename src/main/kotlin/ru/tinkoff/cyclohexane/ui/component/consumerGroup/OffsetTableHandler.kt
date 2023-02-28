package ru.tinkoff.cyclohexane.ui.component.consumerGroup

import ru.tinkoff.cyclohexane.component.kafkaclient.KafkaManager
import java.util.UUID

interface OffsetTableHandler {

    fun commitOffset(cluster: UUID, groupId: String, topic: String, partition: Int, offset: Long)
}


class OffsetTableHandlerImpl(
    private val kafkaManager: KafkaManager
) : OffsetTableHandler {
    override fun commitOffset(cluster: UUID, groupId: String, topic: String, partition: Int, offset: Long) {
        kafkaManager.commitOffset(cluster, groupId, topic, partition, offset)
    }
}