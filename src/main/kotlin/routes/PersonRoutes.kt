package routes

import dto.PersonDto
import services.PersonService

import io.ktor.http.*

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.personRoutes(personService: PersonService) {
    route("/persons") {

        get {
            val lastNameQuery = call.request.queryParameters["lastName"]

            val result = if (lastNameQuery != null) {
                personService.getByLastName(lastNameQuery)
            } else {
                personService.getAll()
            }

            call.respond(HttpStatusCode.OK, result)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Неверный формат ID")
                return@get
            }

            val person = personService.getById(id)
            if (person != null) {
                call.respond(HttpStatusCode.OK, person)
            } else {
                call.respond(HttpStatusCode.NotFound, "Пользователь не найден")
            }
        }

        post {
            val request = runCatching { call.receive<PersonDto>() }.getOrNull()
            if (request == null) {
                call.respond(HttpStatusCode.BadRequest, "Неверный формат данных")
                return@post
            }

            val newPerson = personService.create(request)
            call.respond(HttpStatusCode.Created, newPerson)
        }

        put("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Неверный формат ID")
                return@put
            }

            val request = runCatching { call.receive<PersonDto>() }.getOrNull()
            if (request == null) {
                call.respond(HttpStatusCode.BadRequest, "Неверный формат данных")
                return@put
            }

            val updatedPerson = personService.update(id, request)
            if (updatedPerson != null) {
                call.respond(HttpStatusCode.OK, updatedPerson)
            } else {
                call.respond(HttpStatusCode.NotFound, "Пользователь не найден")
            }
        }
        delete("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Неверный формат ID")
                return@delete
            }

            try {
                val deleted = personService.delete(id)
                if (deleted) {
                    call.respond(HttpStatusCode.OK, "Пользователь успешно помечен как удаленный")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Пользователь не найден или уже удален")
                }
            } catch (e: Exception) {

                call.respond(
                    HttpStatusCode.Conflict,
                    "Ошибка удаления. Скорее всего, к пользователю привязаны телефоны. Текст ошибки БД: ${e.message}"
                )
            }
        }
    }
}