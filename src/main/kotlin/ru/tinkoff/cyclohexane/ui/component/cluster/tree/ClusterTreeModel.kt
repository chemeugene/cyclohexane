package ru.tinkoff.cyclohexane.ui.component.cluster.tree

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import ru.tinkoff.cyclohexane.persistence.entity.ClusterEntity
import ru.tinkoff.cyclohexane.ui.component.cluster.tree.ClusterTreeModel.ItemType.CLUSTER

data class ClusterTreeModel(
    val id: Any,
    val name: String,
    val type: ItemType,
    val parent: ClusterTreeModel? = null
) {
    var children: MutableMap<String, ClusterTreeModel> = mutableStateMapOf()
    var consumerGroups: MutableList<String> = mutableStateListOf()

    enum class ItemType(
        val level: Int
    ) {
        CLUSTER(0), TOPIC(1)
    }
}

fun ClusterEntity.toTreeModel() =
    ClusterTreeModel(id.value, name, CLUSTER)
