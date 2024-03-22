import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import database.DriverFactory
import kotlinx.coroutines.runBlocking
import udesc.eso.ddm.kotlin.TodoDatabase

@OptIn(ExperimentalMaterial3Api::class)
fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "MyTodoListApp2") {
        val db = DriverFactory().createDriver()
        App(db)
    }
}

