package hu.hanprog.backend

import com.auth0.jwt.JWTVerifier
import hu.hanprog.backend.api.ApiInjection
import hu.hanprog.backend.database.DatabaseProvider
import hu.hanprog.backend.database.DatabaseProviderContract
import hu.hanprog.backend.database.injection.DaoInjection
import hu.hanprog.backend.modules.auth.JwtConfig
import hu.hanprog.backend.modules.auth.TokenProvider
import hu.hanprog.backend.modules.injection.ControllersInjection
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun startServer() = embeddedServer(CIO, dotenv["PORT"].toInt()) {
    module {
        install(Koin) {
            slf4jLogger()
            modules(
                module {
                    single<DatabaseProviderContract> { DatabaseProvider() }
                    single<JWTVerifier> { JwtConfig.verifier }
                    single<TokenProvider> { JwtConfig }
                },
                ControllersInjection.koinBeans,
                ApiInjection.koinBeans,
                DaoInjection.koinBeans,
            )
        }
        main()
    }
}.start(true)