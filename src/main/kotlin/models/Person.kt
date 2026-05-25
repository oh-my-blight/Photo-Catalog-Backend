package backend.models

import kotlinx.serialization.Serializable



@Serializable
data class Person(
    val id: Long,
    val firstName: String,
    val patronymic: String? = null,
    val lastName: String,
    val isDeleted: Boolean = false
)
