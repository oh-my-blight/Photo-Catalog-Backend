package backend.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val firstName: String,
    val patronymic: String? = null,
    val lastName: String
)
