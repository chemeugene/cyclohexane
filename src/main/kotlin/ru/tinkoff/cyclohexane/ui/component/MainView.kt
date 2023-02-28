package ru.tinkoff.cyclohexane.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.tinkoff.cyclohexane.ui.common.AppState
import ru.tinkoff.cyclohexane.ui.common.MainContentView
import ru.tinkoff.cyclohexane.ui.component.cluster.tree.ConsumerGroupTreeModel
import ru.tinkoff.cyclohexane.ui.component.consumerGroup.OffsetTable
import ru.tinkoff.cyclohexane.ui.component.consumerGroup.OffsetTableHandler

object MainView : KoinComponent {

    private val offsetTableHandler: OffsetTableHandler by inject()

    @Composable
    fun Panel(appState: AppState) {

        if (appState.mainContentView == MainContentView.TOPIC_VIEW) {
        }
        if (appState.mainContentView == MainContentView.CONSUMER_GROUP) {
            val model = appState.selectedTreeModel as ConsumerGroupTreeModel
            OffsetTable(
                remember(model.id, model.parent!!.id) {
                    model
                },
                offsetTableHandler
            )
        }
    }
}
