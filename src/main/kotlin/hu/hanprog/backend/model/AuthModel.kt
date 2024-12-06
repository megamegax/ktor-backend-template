package hu.hanprog.backend.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentials(val username: String, val password: String)

@Serializable
data class LoginTokenResponse(val credentials: CredentialsResponse)

@Serializable
data class CredentialsResponse(val accessToken: String, val refreshToken: String)

@Serializable
data class RefreshBody(val username: String, val refreshToken: String)