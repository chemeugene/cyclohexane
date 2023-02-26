package ru.tinkoff.cyclohexane.ui.component.topic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.tinkoff.cyclohexane.ui.common.AppState
import ru.tinkoff.cyclohexane.ui.common.MainContentView
import ru.tinkoff.cyclohexane.ui.common.TableScreen
import ru.tinkoff.cyclohexane.ui.common.VerticalScrollbar
import ru.tinkoff.cyclohexane.ui.component.cluster.tree.ConsumerGroupTreeModel

object TopicMainView {

    @Composable
    fun Panel(appState: AppState) {
        Box {
            val scrollState = rememberLazyListState()

            if (appState.mainContentView == MainContentView.TOPIC_VIEW) {
            }
            if (appState.mainContentView == MainContentView.CONSUMER_GROUP) {
                val model = appState.selectedTreeModel as ConsumerGroupTreeModel
                TableScreen(model.groupOffsets)
            }
            VerticalScrollbar(
                Modifier.align(Alignment.CenterEnd),
                scrollState
            )
        }
    }
}
