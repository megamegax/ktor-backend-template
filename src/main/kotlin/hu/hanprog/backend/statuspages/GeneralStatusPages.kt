package hu.hanprog.backend.statuspages

import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun StatusPagesConfig.generalStatusPages() {
    exception<MissingArgumentException> { call, cause ->
        call.respond(HttpStatusCode.BadRequest, cause.message)
    }
}

data class MissingArgumentException(override val message: String = "Missing argument") : Exception()