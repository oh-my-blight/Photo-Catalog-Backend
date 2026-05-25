package database


import org.jetbrains.exposed.dao.id.LongIdTable

object PersonsTable : LongIdTable("persons", "id") {
    val firstName = text("first_name")
    val patronymic = text("patronymic").nullable()
    val lastName = text("last_name")
    val isDeleted = bool("is_deleted").default(false)
}