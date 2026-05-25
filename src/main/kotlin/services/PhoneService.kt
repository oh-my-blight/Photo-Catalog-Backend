package services
import database.PersonPhonesTable
import dto.PhoneDto
import kotlinx.coroutines.Dispatchers
import models.Phone
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class PhoneService {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    private fun resultRowToPhone(row: ResultRow) = Phone(
        id = row[PersonPhonesTable.id].value,
        personId = row[PersonPhonesTable.personId].value,
        phoneTypeId = row[PersonPhonesTable.phoneTypeId],
        countryCode = row[PersonPhonesTable.countryCode],
        areaCode = row[PersonPhonesTable.areaCode],
        number = row[PersonPhonesTable.number]
    )


    suspend fun getAll(): List<Phone> = dbQuery {
        PersonPhonesTable.selectAll().map(::resultRowToPhone)
    }


    suspend fun getByPersonId(personId: Long): List<Phone> = dbQuery {
        PersonPhonesTable
            .selectAll().where { PersonPhonesTable.personId eq personId }
            .map(::resultRowToPhone)
    }


    suspend fun getById(id: Long): Phone? = dbQuery {
        PersonPhonesTable
            .selectAll().where { PersonPhonesTable.id eq id }
            .map(::resultRowToPhone)
            .singleOrNull()
    }


    suspend fun create(dto: PhoneDto): Phone = dbQuery {
        val insertStatement = PersonPhonesTable.insert {
            it[personId] = dto.personId
            it[phoneTypeId] = dto.phoneTypeId
            it[countryCode] = dto.countryCode
            it[areaCode] = dto.areaCode
            it[number] = dto.number
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToPhone)
            ?: throw Exception("Не удалось добавить телефон")
    }


    suspend fun update(id: Long, dto: PhoneDto): Phone? = dbQuery {
        val updatedRows = PersonPhonesTable.update({ PersonPhonesTable.id eq id }) {
            it[personId] = dto.personId
            it[phoneTypeId] = dto.phoneTypeId
            it[countryCode] = dto.countryCode
            it[areaCode] = dto.areaCode
            it[number] = dto.number
        }

        if (updatedRows > 0) getById(id) else null
    }

    suspend fun deletePhone(id: Long): Boolean = dbQuery {
        PersonPhonesTable.deleteWhere { PersonPhonesTable.id eq id } > 0
    }
}