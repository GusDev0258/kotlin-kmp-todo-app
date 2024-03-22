package database

import app.cash.sqldelight.db.SqlDriver
import udesc.eso.ddm.kotlin.TodoDatabase

expect class DriverFactory {
    fun createDriver(): SqlDriver
}
fun createDataBase(driverFactory: DriverFactory): TodoDatabase{
    val driver = driverFactory.createDriver()
    val database = TodoDatabase(driver)
    return database
}