package ru.tinkoff.cyclohexane.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tinkoff.cyclohexane.component.kafkaclient.KafkaOffset

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .padding(8.dp),
        fontSize = 10.sp
    )
}

@Composable
fun TableScreen(data: List<KafkaOffset>) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Row(Modifier.background(Color.Gray)) {
                TableCell(text = "Topic", weight = .3f)
                TableCell(text = "Partition", weight = .3f)
                TableCell(text = "Offset", weight = .3f)
            }
        }
        items(data.size) {
            val (topic, partition, offset) = data[it]
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = topic, weight = .3f)
                TableCell(text = partition.toString(), weight = .3f)
                TableCell(text = offset.toString(), weight = .3f)
            }
        }
    }
}