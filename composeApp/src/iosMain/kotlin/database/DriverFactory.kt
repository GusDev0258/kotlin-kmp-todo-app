package database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import udesc.eso.ddm.kotlin.TodoDatabase

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(TodoDatabase.Schema, "app.db")
    }
}