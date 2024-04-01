package database.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import repository.TodoRepositoryProtocol
import udesc.eso.ddm.kotlin.TodoDatabase
import udesc.eso.ddm.kotlin.database.TodoEntity

class TodoRepositoryImpl(sqlDriver: SqlDriver) : TodoRepositoryProtocol {
    val dbInstance = TodoDatabase(sqlDriver);
    override fun registerTodo(title: String, description: String, dueDate: String) {
        try {
            this.dbInstance.todoDatabaseQueries.createTodo(
                title = title,
                description = description,
                created_at = LocalDate.toString(),
                dueDate = dueDate,
                isCompleted = 0
            )
        } catch (e: Exception) {
            println("Error inserting todo: ${e.message}")
        }
    }

    override fun getAllTodos(): Flow<List<TodoEntity>> = dbInstance.todoDatabaseQueries.selectAllTodos().asFlow().mapToList(Dispatchers.IO)

    override fun getTodoById(todoId: Long): TodoEntity? {
        try {
            return this.dbInstance.todoDatabaseQueries.selectTodoById(todoId).executeAsOne()
        } catch (e: Exception) {
            println("Error getting todo: ${e.message}")
            return null
        }
    }

    override fun changeTodoById(todoId: Long, title: String, description: String, dueDate: String) {
        try {
            this.dbInstance.todoDatabaseQueries.changeTodoById(
                title,
                description,
                dueDate,
                LocalDate.toString(),
                todoId
            )
        } catch (e: Exception) {
            println("Error changing todo attributes: ${e.message}")
        }
    }

    override fun deleteTodoById(todoId: Long) {
        try {
            this.dbInstance.todoDatabaseQueries.deleteById(todoId)
        } catch (e: Exception) {
            println("Error deleting todo: ${e.message}")
        }
    }

    override fun completeTodo(id: Long) {
        try {
            this.dbInstance.todoDatabaseQueries.completeTodoById(id)
            println("Updated todo of $id")
        } catch (e: Exception) {
            println("Error marking todo as completed: ${e.message}")
        }
    }

    override fun unCompleteTodo(id: Long) {
        try {
            this.dbInstance.todoDatabaseQueries.unCompleteTodoById(id)
            println("Updated todo of $id")
        } catch (e: Exception) {
            println("Error unmarking todo as completed: ${e.message}")
        }
    }

    override fun getCompletedTodos(): List<TodoEntity> {
        try {
            return this.dbInstance.todoDatabaseQueries.selectCompletedTodos().executeAsList()
        } catch (e: Exception) {
            println("Error returning all completed todos: ${e.message}")
            return emptyList()
        }
    }

    override fun getUnCompletedTodos(): List<TodoEntity> {
        try {
            return this.dbInstance.todoDatabaseQueries.selectNotCompletedTodos().executeAsList()
        } catch (e: Exception) {
            println("Error returning all not completed todos: ${e.message}")
            return emptyList()
        }
    }
}