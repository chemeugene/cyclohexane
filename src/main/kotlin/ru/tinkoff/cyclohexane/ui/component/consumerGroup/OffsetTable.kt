package ru.tinkoff.cyclohexane.ui.component.consumerGroup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tinkoff.cyclohexane.ui.common.VerticalScrollbar
import ru.tinkoff.cyclohexane.ui.component.cluster.tree.ConsumerGroupTreeModel

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
fun RowScope.TableCellWithTextField(
    text: String,
    weight: Float,
    valueChangeListener: (String) -> Unit
) {
    TextField(
        modifier = Modifier.padding(10.dp)
            .fillMaxWidth()
            .height(50.dp)
            .weight(weight),
        value = text,
        onValueChange = valueChangeListener,
        textStyle = TextStyle(fontSize = 10.sp)
    )
}

@Composable
fun RowScope.TableCellWithButton(
    text: String,
    weight: Float,
    clickListener: () -> Unit
) {
    Button(
        modifier = Modifier
            .padding(5.dp)
            .weight(weight),
        onClick = clickListener
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(8.dp),
            fontSize = 10.sp
        )
    }

}

@Composable
fun OffsetTable(model: ConsumerGroupTreeModel, handler: OffsetTableHandler) {
    Box {
        val scrollState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            state = scrollState
        ) {
            item {
                Row(Modifier.background(Color.Gray)) {
                    TableCell(text = "Topic", weight = .3f)
                    TableCell(text = "Partition", weight = .3f)
                    TableCell(text = "Offset", weight = .3f)
                    TableCell(text = "Apply", weight = .1f)
                }
            }
            items(model.groupOffsets.size) {
                val viewModel = remember(model.id) { KafkaOffsetModel.of(model.groupOffsets[it]) }


                Row(Modifier.fillMaxWidth()) {
                    TableCell(text = viewModel.topic.value, weight = .3f)
                    TableCell(text = viewModel.partition.value, weight = .3f)
                    TableCellWithTextField(text = viewModel.offset.value, weight = .3f, valueChangeListener = { value ->
                        viewModel.offset.value = value
                        model.groupOffsets[it].offset = value.toLong()
                    })
                    TableCellWithButton(text = "Commit", weight = .1f, clickListener = {
                        handler.commitOffset(
                            cluster = model.clusterId(),
                            groupId = model.name,
                            topic = viewModel.topic.value,
                            partition = viewModel.partition.value.toInt(),
                            offset = viewModel.offset.value.toLong()
                        )
                    })
                }
            }
        }
        VerticalScrollbar(
            Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            scrollState
        )
    }
}