package hu.hanprog.backend.modules.auth

import com.auth0.jwt.JWTVerifier
import hu.hanprog.backend.api.user.UserApi
import hu.hanprog.backend.database.DatabaseProviderContract
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.jwt.jwt


fun AuthenticationConfig.authenticationModule(
    userApi: UserApi,
    databaseProvider: DatabaseProviderContract,
    tokenVerifier: JWTVerifier
) {
    /**
     * Setup the JWT authentication to be used in [Routing].
     * If the token is valid, the corresponding [User] is fetched from the database.
     * The [User] can then be accessed in each [ApplicationCall].
     */
    jwt("jwt") {
        verifier(tokenVerifier)
        realm = "ktor.io"
        validate {
            it.payload.getClaim("id").asInt()?.let { userId ->
                // do database query to find Principal subclass
                databaseProvider.dbQuery {
                    userApi.getUserById(userId)
                }
            }
        }
    }
}