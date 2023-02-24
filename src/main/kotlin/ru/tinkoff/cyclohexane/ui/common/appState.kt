package ru.tinkoff.cyclohexane.ui.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ru.tinkoff.cyclohexane.persistence.entity.ClusterEntity
import ru.tinkoff.cyclohexane.ui.common.MainContentView.NOTHING
import ru.tinkoff.cyclohexane.ui.component.cluster.tree.ClusterTreeModel
import ru.tinkoff.cyclohexane.ui.component.cluster.tree.TopicTreeModel
import ru.tinkoff.cyclohexane.ui.component.cluster.tree.TreeModel
import ru.tinkoff.cyclohexane.ui.component.cluster.tree.toTreeModel

object AppState {

    var mainContentView: MainContentView by mutableStateOf(NOTHING)

    var clusterTreeModel: MutableList<ClusterTreeModel> = mutableStateListOf<ClusterTreeModel>()
        .also {
            it.addAll(ClusterEntity.findAll().map { entity -> entity.toTreeModel() })
        }

    var selectedTreeModel by mutableStateOf<TreeModel?>(null)

    fun isClusterSelected() = selectedTreeModel is ClusterTreeModel

    fun isTopicSelected() = selectedTreeModel is TopicTreeModel
}
