package database


import org.jetbrains.exposed.dao.id.LongIdTable

object PersonPhonesTable : LongIdTable("person_phones", "id") {
    val personId = reference("person_id", PersonsTable)
    val phoneTypeId = long("phone_type_id")
    val countryCode = text("country_code")
    val areaCode = text("area_code")
    val number = text("number")
}