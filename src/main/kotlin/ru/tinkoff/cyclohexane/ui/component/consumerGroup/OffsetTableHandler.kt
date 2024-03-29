package ru.tinkoff.cyclohexane.ui.component.consumerGroup

import mu.KLogging
import ru.tinkoff.cyclohexane.component.kafkaclient.KafkaManager
import java.util.UUID

interface OffsetTableHandler {

    fun commitOffset(cluster: UUID, groupId: String, topic: String, partition: Int, offset: Long)
}


class OffsetTableHandlerImpl(
    private val kafkaManager: KafkaManager
) : OffsetTableHandler {
    override fun commitOffset(cluster: UUID, groupId: String, topic: String, partition: Int, offset: Long) {
        runCatching {
            kafkaManager.commitOffset(cluster, groupId, topic, partition, offset)
        }.onFailure {
            logger.error(it) { "Error commit offset" }
        }.getOrThrow()

    }

    companion object : KLogging()
}