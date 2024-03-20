package database

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import udesc.eso.ddm.kotlin.TodoDatabase

actual class DriverFactory actual constructor() {
    actual suspend fun createDriver(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        TodoDatabase.Schema.awaitCreate(driver)
        return driver
    }
}