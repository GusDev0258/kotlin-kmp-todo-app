import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.window.ComposeUIViewController
import database.DriverFactory
import kotlinx.coroutines.runBlocking
import udesc.eso.ddm.kotlin.TodoDatabase

@OptIn(ExperimentalMaterial3Api::class)
fun MainViewController() = ComposeUIViewController {
    val db = DriverFactory().createDriver()
    App(db)
}
