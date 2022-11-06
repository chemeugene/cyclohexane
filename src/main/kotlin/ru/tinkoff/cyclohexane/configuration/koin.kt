package ru.tinkoff.cyclohexane.configuration

import org.koin.dsl.module
import ru.tinkoff.cyclohexane.component.kafkaclient.ClientFactory
import ru.tinkoff.cyclohexane.component.kafkaclient.ClientFactoryImpl
import ru.tinkoff.cyclohexane.component.kafkaclient.KafkaManager
import ru.tinkoff.cyclohexane.component.kafkaclient.KafkaManagerImpl
import ru.tinkoff.cyclohexane.persistence.dao.MigrationService
import ru.tinkoff.cyclohexane.persistence.dao.impl.MigrationServiceImpl
import ru.tinkoff.cyclohexane.ui.component.cluster.tree.ClusterTreeHandler
import ru.tinkoff.cyclohexane.ui.component.cluster.tree.ClusterTreeHandlerImpl

val appModule = module {
    single<MigrationService> { MigrationServiceImpl() }
    single<ClusterTreeHandler> { ClusterTreeHandlerImpl(get()) }
    single<ClientFactory> { ClientFactoryImpl() }
    single<KafkaManager> { KafkaManagerImpl() }
}
