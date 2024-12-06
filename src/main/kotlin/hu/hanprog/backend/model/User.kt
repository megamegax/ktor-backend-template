package hu.hanprog.backend.model


import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val fullname: String,
    val username: String,
    val password: String
)

@Serializable
data class PostUserBody(
    val fullname: String,
    val username: String,
    val password: String
)

@Serializable
data class PutUserBody(
    val fullname: String? = null,
    val username: String? = null
)

@Serializable
data class LoginUserBody(
    val username: String,
    val password: String,
)