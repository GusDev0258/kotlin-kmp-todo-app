package database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import udesc.eso.ddm.kotlin.TodoDatabase

actual class DriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return NativeSqliteDriver(TodoDatabase.Schema.synchronous(), "app.db")
    }
}