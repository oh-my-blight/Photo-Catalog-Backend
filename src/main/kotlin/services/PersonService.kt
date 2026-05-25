package services


import database.PersonsTable
import dto.PersonDto
import kotlinx.coroutines.Dispatchers
import models.Person
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class PersonService {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    private fun resultRowToPerson(row: ResultRow) = Person(
        id = row[PersonsTable.id].value,
        firstName = row[PersonsTable.firstName],
        patronymic = row[PersonsTable.patronymic],
        lastName = row[PersonsTable.lastName],
        isDeleted = row[PersonsTable.isDeleted]
    )

    suspend fun getAll(): List<Person> = dbQuery {
        PersonsTable
            .selectAll()
            .where { PersonsTable.isDeleted eq false }
            .map(::resultRowToPerson)
    }

    suspend fun getByLastName(lastName: String): List<Person> = dbQuery {
        PersonsTable
            .selectAll()
            .where { (PersonsTable.lastName eq lastName) and (PersonsTable.isDeleted eq false) }
            .map(::resultRowToPerson)
    }

    suspend fun getById(id: Long): Person? = dbQuery {
        PersonsTable
            // ИСПРАВЛЕНИЕ ЗДЕСЬ
            .selectAll()
            .where { (PersonsTable.id eq id) and (PersonsTable.isDeleted eq false) }
            .map(::resultRowToPerson)
            .singleOrNull()
    }

    suspend fun create(dto: PersonDto): Person = dbQuery {
        val insertStatement = PersonsTable.insert {
            it[firstName] = dto.firstName
            it[patronymic] = dto.patronymic
            it[lastName] = dto.lastName
            it[isDeleted] = false
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToPerson)
            ?: throw Exception("Не удалось создать пользователя")
    }

    suspend fun update(id: Long, dto: PersonDto): Person? = dbQuery {
        val updatedRows = PersonsTable.update({ (PersonsTable.id eq id) and (PersonsTable.isDeleted eq false) }) {
            it[firstName] = dto.firstName
            it[patronymic] = dto.patronymic
            it[lastName] = dto.lastName
        }

        if (updatedRows > 0) getById(id) else null
    }

    suspend fun delete(id: Long): Boolean = dbQuery {
        val updatedRows = PersonsTable.update({ (PersonsTable.id eq id) and (PersonsTable.isDeleted eq false) }) {
            it[isDeleted] = true
        }
        updatedRows > 0
    }
}