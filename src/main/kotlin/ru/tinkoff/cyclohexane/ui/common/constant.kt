package ru.tinkoff.cyclohexane.ui.common

const val SECURITY_TYPE_DN = "Security type"

const val CLUSTER_ADD_BTN = "Add"
const val CLUSTER_EDIT_BTN = "Edit"
const val CLUSTER_DELETE_BTN = "Delete"
const val CLUSTER_SAVE_BTN = "Save"
const val CONNECT_BTN = "Connect"
const val DISCONNECT_BTN = "Disconnect"

const val CLUSTER_NAME = "Cluster name"
const val BOOTSTRAP_SERVERS = "Bootstrap servers"
const val SASL_MECHANISM = "SASL Mechanism"
const val SCHEMA_REGISTRY = "Schema registry endpoint"
const val JAAS_CONFIG = "JAAS Config"

enum class SecurityType(
    private val type: String) {

    PLAINTEXT("Plaintext"),
    SASL_PLAINTEXT("SASL Plaintext"),
    SSL("SSL"),
    SASL_SSL("SASL SSL")
}

enum class MainContentView {
    CLUSTER_PROPERTIES_FORM,
    TOPIC_VIEW,
    NOTHING
}
