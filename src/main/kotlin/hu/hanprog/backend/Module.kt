package hu.hanprog.backend

import hu.hanprog.backend.api.user.UserApi
import hu.hanprog.backend.modules.auth.JwtConfig
import hu.hanprog.backend.database.DatabaseProviderContract
import hu.hanprog.backend.modules.auth.authenticationModule
import hu.hanprog.backend.modules.user.loginModule
import hu.hanprog.backend.modules.user.userModule
import hu.hanprog.backend.statuspages.authStatusPages
import hu.hanprog.backend.statuspages.generalStatusPages
import hu.hanprog.backend.statuspages.userStatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.event.Level


fun Application.module() {
    val userApi by inject<UserApi>()
    val databaseProvider by inject<DatabaseProviderContract>()
    databaseProvider.init()
    install(CallLogging) {
        level = Level.INFO
    }
    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        generalStatusPages()
        userStatusPages()
        authStatusPages()
        exception<UnknownError> { call, _ ->
            call.respondText(
                "Internal server error",
                ContentType.Text.Plain,
                status = HttpStatusCode.InternalServerError
            )
        }
        exception<IllegalArgumentException> { call, _ ->
            call.respondText(HttpStatusCode.BadRequest.description)
        }
    }
    install(Authentication) {
        authenticationModule(userApi, databaseProvider, JwtConfig.verifier)
    }
    routing {
        loginModule()
        authenticate("jwt") {
            userModule()
        }
    }
}


fun Application.main() {
    module()
}