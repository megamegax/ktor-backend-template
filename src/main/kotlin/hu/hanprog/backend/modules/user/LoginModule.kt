package hu.hanprog.backend.modules.user

import hu.hanprog.backend.model.LoginUserBody
import hu.hanprog.backend.modules.auth.JwtConfig
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject


fun Route.loginModule() {

    val controller by inject<UserController>()

    post<LoginUserBody>("login") { loginUserBody ->
        val user = controller.findUserByCredentials(loginUserBody.username, loginUserBody.password)
        if (user != null) {
            val tokens = JwtConfig.createTokens(user)
            call.respond(tokens)
         //   call.respond(message = tokens, typeInfo = TypeInfo(CredentialsResponse::class))
        } else {
            call.respond(HttpStatusCode.NotFound, "User not found")
        }
    }
}