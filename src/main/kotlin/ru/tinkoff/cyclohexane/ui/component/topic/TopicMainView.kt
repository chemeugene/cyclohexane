package ru.tinkoff.cyclohexane.ui.component.topic

import androidx.compose.runtime.Composable
import ru.tinkoff.cyclohexane.ui.common.AppState
import ru.tinkoff.cyclohexane.ui.common.MainContentView

object TopicMainView {

    @Composable
    fun Panel(appState: AppState) {
        if (appState.mainContentView == MainContentView.TOPIC_VIEW) {
        }
    }
}
