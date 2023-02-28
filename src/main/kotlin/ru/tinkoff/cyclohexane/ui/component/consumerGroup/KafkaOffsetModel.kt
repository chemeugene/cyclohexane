package ru.tinkoff.cyclohexane.ui.component.consumerGroup

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ru.tinkoff.cyclohexane.component.kafkaclient.KafkaOffset

data class KafkaOffsetModel(
    var topic: MutableState<String>,
    var partition: MutableState<String>,
    var offset: MutableState<String>
) {
    companion object {
        fun of(model: KafkaOffset) = KafkaOffsetModel(
            mutableStateOf(model.topic),
            mutableStateOf(model.partition.toString()),
            mutableStateOf(model.offset.toString())
        )
    }
}
