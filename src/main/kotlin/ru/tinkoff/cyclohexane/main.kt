import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import ru.tinkoff.cyclohexane.configuration.appModule
import ru.tinkoff.cyclohexane.persistence.dao.MigrationService
import ru.tinkoff.cyclohexane.ui.common.AppTheme
import ru.tinkoff.cyclohexane.ui.component.MainContainer

fun main() {
    startKoin {
        modules(appModule)
    }
    Application().start()
}

class Application : KoinComponent {

    private val migrationService: MigrationService by inject()

    fun start() {
        migrationService.migrate()
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "Cyclohexane UI",
                state = rememberWindowState(width = 900.dp, height = 600.dp)
            ) {
                MaterialTheme(
                    colors = AppTheme.colors.material
                ) {
                    Surface {
                        MainContainer()
                    }
                }
            }
        }
    }
}
