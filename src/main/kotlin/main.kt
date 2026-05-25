package backend;

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class StatusResponse(val status: String, val database: String)

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {

        install(ContentNegotiation) {
            json()
        }

        routing {

            get("/") {
                call.respondText("Ktor сервер работает!")
            }

            get("/health") {
                val response = StatusResponse(
                    status = "OK",
                    database = "Disconnected (Test mode)"
                )
                call.respond(response)
            }
        }

    }.start(wait = true)
}