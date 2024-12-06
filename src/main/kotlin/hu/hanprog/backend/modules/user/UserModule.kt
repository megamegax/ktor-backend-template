package hu.hanprog.backend.modules.user

import hu.hanprog.backend.model.PutUserBody
import hu.hanprog.backend.model.toResponseUser
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun RoutingCall.userId(): Int {
    return principal<JWTPrincipal>()!!.payload.getClaim("userId").asInt()
}

fun Route.userModule() {

    val controller by inject<UserController>()

    get("me") {
        val userId = call.userId()
        val user = controller.findUserById(userId)
        if (user != null) {
            call.respond(user.toResponseUser())
        } else {
            call.respond(HttpStatusCode.NotFound, "User not found")
        }
    }

    put("updateProfile") {
        val putUser = call.receive<PutUserBody>()
        val user =
            controller.updateProfile(call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asInt(), putUser)
        call.respond(HttpStatusCode.OK)
    }

    delete("user") {
        controller.removeUser(call.userId())
        call.respond(status = HttpStatusCode.OK, message = "User deleted")
    }
}