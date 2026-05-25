package routes
import dto.PhoneDto
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import services.PhoneService

fun Route.phoneRoutes(phoneService: PhoneService) {
    route("/phones") {


        get {
            val personIdQuery = call.request.queryParameters["personId"]?.toLongOrNull()

            val result = if (personIdQuery != null) {
                phoneService.getByPersonId(personIdQuery)
            } else {
                phoneService.getAll()
            }

            call.respond(HttpStatusCode.OK, result)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Неверный формат ID")
                return@get
            }

            val phone = phoneService.getById(id)
            if (phone != null) {
                call.respond(HttpStatusCode.OK, phone)
            } else {
                call.respond(HttpStatusCode.NotFound, "Телефон не найден")
            }
        }

        post {
            val request = runCatching { call.receive<PhoneDto>() }.getOrNull()
            if (request == null) {
                call.respond(HttpStatusCode.BadRequest, "Неверный формат данных")
                return@post
            }

            try {
                val newPhone = phoneService.create(request)
                call.respond(HttpStatusCode.Created, newPhone)
            } catch (e: Exception) {

                call.respond(HttpStatusCode.Conflict, "Ошибка сохранения БД: ${e.message}")
            }
        }

        put("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Неверный формат ID")
                return@put
            }

            val request = runCatching { call.receive<PhoneDto>() }.getOrNull()
            if (request == null) {
                call.respond(HttpStatusCode.BadRequest, "Неверный формат данных")
                return@put
            }

            try {
                val updatedPhone = phoneService.update(id, request)
                if (updatedPhone != null) {
                    call.respond(HttpStatusCode.OK, updatedPhone)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Телефон не найден")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, "Ошибка обновления БД: ${e.message}")
            }
        }


        delete("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Неверный формат ID")
                return@delete
            }

            val deleted = phoneService.deletePhone(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK, "Телефон успешно удален")
            } else {
                call.respond(HttpStatusCode.NotFound, "Телефон не найден")
            }
        }
    }
}