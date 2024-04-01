package repository

import kotlinx.coroutines.flow.Flow
import udesc.eso.ddm.kotlin.database.TodoEntity

interface TodoRepositoryProtocol {
    fun registerTodo(title: String, description: String, dueDate: String)
    fun getAllTodos(): Flow<List<TodoEntity>>
    fun getTodoById(todoId: Long): TodoEntity?
    fun changeTodoById(todoId: Long, title: String, description: String, dueDate: String)
    fun deleteTodoById(todoId: Long)
    fun completeTodo(todoId: Long)
    fun unCompleteTodo(todoId: Long)
    fun getCompletedTodos(): List<TodoEntity>
    fun getUnCompletedTodos(): List<TodoEntity>
}