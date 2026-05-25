package dto

import kotlinx.serialization.Serializable

@Serializable
data class PhoneDto(
    val personId: Long,
    val phoneTypeId: Long,
    val countryCode: String,
    val areaCode: String,
    val number: String
)