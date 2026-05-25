package models

import kotlinx.serialization.Serializable

@Serializable
data class Phone(
    val id: Long,
    val personId: Long,
    val phoneTypeId: Long,
    val countryCode: String,
    val areaCode: String,
    val number: String
)