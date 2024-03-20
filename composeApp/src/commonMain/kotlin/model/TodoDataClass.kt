package model

import kotlinx.serialization.Serializable

@Serializable
data class TodoDataClass(
    val id: Int,
    val title: String,
    val description: String?,
    val isCompleted: Int = 0,
    val dueDate: String?,
    val created_at: String?,
    val updated_at: String?
)
