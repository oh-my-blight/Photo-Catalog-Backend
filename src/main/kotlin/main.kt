import factories.DatabaseFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.* import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import routes.personRoutes
import routes.phoneRoutes
import services.PersonService
import services.PhoneService

@Serializable
data class StatusResponse(val status: String, val database: String)

fun main() {
    DatabaseFactory.configure()
        .url("jdbc:postgresql://localhost:5429/postgres")
        .user("postgres")
        .password("1234")
        .connect()

    val personService = PersonService()
    val phoneService = PhoneService()

    embeddedServer(Netty, port = 8080, host = "localhost") {

        install(ContentNegotiation) {
            json()
        }

        install(CORS) {
            anyHost()
            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Delete)
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.Authorization)
            allowCredentials = true
        }

        routing {
            get("/") {
                call.respondText("API работает")
            }

            swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
            openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml")

            get("/health") {
                val response = StatusResponse(status = "OK", database = "Connected")
                call.respond(response)
            }

            personRoutes(personService)
            phoneRoutes(phoneService)
        }
    }.start(wait = true)
}