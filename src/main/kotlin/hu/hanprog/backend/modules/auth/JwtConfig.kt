package hu.hanprog.backend.modules.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import hu.hanprog.backend.dotenv
import hu.hanprog.backend.model.CredentialsResponse
import hu.hanprog.backend.model.User
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

object JwtConfig : TokenProvider {

    private val secret = dotenv["JWT_SECRET"]
    private const val issuer = "Backend"
    private val validityInMs: Long = dotenv["JWT_VALIDITY"].toLong()
    private val refreshValidityInMs: Long = dotenv["JWT_VALIDITY"].toLong()
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    override fun verifyToken(token: String): Int? {
        return verifier.verify(token).claims["id"]?.asInt()
    }

    /**
     * Produce token and refresh token for this combination of User and Account
     */
    override fun createTokens(user: User) = CredentialsResponse(
        createToken(user, getTokenExpiration()),
        createToken(user, getTokenExpiration(refreshValidityInMs))
    )

    private fun createToken(user: User, expiration: LocalDateTime) = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.id)
        .withClaim("name", user.fullname)
        .withExpiresAt(expiration.toInstant(ZoneOffset.UTC))
        .sign(algorithm)

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getTokenExpiration(validity: Long = validityInMs) =
        LocalDateTime.now().plus(validity, ChronoUnit.MILLIS)
            .atOffset(ZoneOffset.UTC)
            .toLocalDateTime()
}

interface TokenProvider {
    fun createTokens(user: User): CredentialsResponse
    fun verifyToken(token: String): Int?
}