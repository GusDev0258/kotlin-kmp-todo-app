package udesc.eso.ddm

import App
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.cash.sqldelight.db.SqlDriver
import database.DriverFactory

class MainActivity : ComponentActivity() {
    private lateinit var sqlDriver: SqlDriver
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sqlDriver = DriverFactory(this).createDriver()
        setContent {
            App(sqlDriver)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AppAndroidPreview() {
    val db = DriverFactory(Application()).createDriver()
    App(db)
}

