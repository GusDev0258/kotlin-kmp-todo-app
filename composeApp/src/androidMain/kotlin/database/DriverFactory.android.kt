package database

import android.app.Application
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import udesc.eso.ddm.kotlin.TodoDatabase

actual class DriverFactory actual constructor() {
    actual suspend fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(TodoDatabase.Schema.synchronous(), Application(), "app.db")
    }
}