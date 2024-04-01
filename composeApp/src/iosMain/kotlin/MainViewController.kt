import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.window.ComposeUIViewController
import database.DriverFactory
import database.repository.TodoRepositoryImpl
import kotlinx.coroutines.runBlocking
import repository.TodoRepositoryProtocol
import udesc.eso.ddm.kotlin.TodoDatabase

@OptIn(ExperimentalMaterial3Api::class)
fun MainViewController() = ComposeUIViewController {
    val IosSqlDriver = DriverFactory().createDriver()
    val todoRepository: TodoRepositoryProtocol = TodoRepositoryImpl(IosSqlDriver)
    App(todoRepository)
}
