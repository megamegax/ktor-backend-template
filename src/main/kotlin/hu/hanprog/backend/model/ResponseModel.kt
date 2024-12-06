package hu.hanprog.backend.model


data class ResponseUser(
    val id: Int,
    val fullname: String,
    val username: String
)

fun User.toResponseUser() = ResponseUser(
    this.id,
    this.fullname,
    this.username
)