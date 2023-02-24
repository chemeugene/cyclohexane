package ru.tinkoff.cyclohexane.ui.component.cluster.tree

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.tinkoff.cyclohexane.persistence.entity.ClusterEntity

data class ClusterTreeModel(
    override val id: Any,
    override val name: String,
    override val parent: TreeModel?,
    override val padding: Dp = 0.dp
) : TreeModel, Expandable, Connectable {

    private var expanded: Boolean by mutableStateOf(false)
    var connected: Boolean by mutableStateOf(false)
    override val connectable: Boolean = true

    var topics: TopicListTreeModel? by mutableStateOf(null)
    var consumerGroups: ConsumerGroupListTreeModel? by mutableStateOf(null)

    fun disconnect() {
        topics = null
        consumerGroups = null
        connected = false
    }

    override fun isExpanded(): Boolean = expanded

    override fun toggleExpanded() {
        expanded = !expanded
    }

    override fun isConnected(): Boolean = connected

    override fun toggleConnect() {
        connected = !connected
    }
}

data class TopicTreeModel(
    override val id: Any,
    override val name: String,
    override val parent: TreeModel?,
    override val padding: Dp = 10.dp
) : TreeModel {
    override val expandable = false

}

data class TopicListTreeModel(
    override val id: Any = "Topics",
    override val name: String = "Topics",
    override val parent: TreeModel?,
    override val padding: Dp = 5.dp
) : TreeModel, Expandable {
    private var expanded: Boolean by mutableStateOf(false)
    var topics: MutableList<TopicTreeModel> = mutableStateListOf()

    override fun isExpanded(): Boolean = expanded

    override fun toggleExpanded() {
        expanded = !expanded
    }
}

data class ConsumerGroupTreeModel(
    override val id: Any,
    override val name: String,
    override val parent: TreeModel?,
    override val padding: Dp = 10.dp
) : TreeModel {
    override val expandable = false
}

data class ConsumerGroupListTreeModel(
    override val id: Any = "ConsumerGroups",
    override val name: String = "Consumer Groups",
    override val parent: TreeModel?,
    override val padding: Dp = 5.dp
) : TreeModel, Expandable {
    private var expanded: Boolean by mutableStateOf(false)
    var consumerGroups: MutableList<ConsumerGroupTreeModel> = mutableStateListOf()

    override fun isExpanded(): Boolean = expanded

    override fun toggleExpanded() {
        expanded = !expanded
    }
}

interface TreeModel {
    val id: Any
    val name: String
    val parent: TreeModel?
    val expandable: Boolean
        get() = true
    val connectable: Boolean
        get() = false

    val padding: Dp
}

interface Expandable {
    fun isExpanded(): Boolean

    fun toggleExpanded() {}
}

interface Connectable {
    fun isConnected(): Boolean

    fun toggleConnect() {}
}

fun ClusterEntity.toTreeModel() =
    ClusterTreeModel(id.value, name, null)
