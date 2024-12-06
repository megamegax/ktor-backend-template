package hu.hanprog.backend.database

import hu.hanprog.backend.dotenv
import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.DATABASE
import io.r2dbc.spi.ConnectionFactoryOptions.DRIVER
import io.r2dbc.spi.ConnectionFactoryOptions.HOST
import io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD
import io.r2dbc.spi.ConnectionFactoryOptions.PORT
import io.r2dbc.spi.ConnectionFactoryOptions.PROTOCOL
import io.r2dbc.spi.ConnectionFactoryOptions.USER
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import java.time.Duration
import kotlin.coroutines.CoroutineContext

@OptIn(ObsoleteCoroutinesApi::class)
class DatabaseProvider : DatabaseProviderContract, KoinComponent {

    private val dispatcher: CoroutineContext
    private val pooledConnectionFactory: ConnectionFactory
    private val pool: ConnectionPool

    init {
        dispatcher = newFixedThreadPoolContext(5, "database-pool")
        pooledConnectionFactory = ConnectionFactories.get(
            ConnectionFactoryOptions.builder()
                .option(DRIVER, "pool")
                .option(PROTOCOL, "mysql") // driver identifier, PROTOCOL is delegated as DRIVER by the pool.
                .option(HOST, dotenv["DB_HOST"])
                .option(PORT, dotenv["DB_PORT"].toInt())
                .option(USER, dotenv["DB_USERNAME"])
                .option(PASSWORD, dotenv["DB_PASSWORD"])
                .option(DATABASE, dotenv["DB_NAME"])
                .option(ConnectionFactoryOptions.SSL, true)
                .build()
        )
        val configuration: ConnectionPoolConfiguration = ConnectionPoolConfiguration.builder(pooledConnectionFactory)
            .maxIdleTime(Duration.ofMillis(1000))
            .maxSize(20)
            .build();

        pool = ConnectionPool(configuration);
    }

    override fun init() {

    }

    override suspend fun <T> dbQuery(block: suspend (Connection) -> T): T = withContext(dispatcher) {
        val connection = pool.create().awaitSingleOrNull()
        if (connection == null) {
            throw Exception("No connection available")
        }
        val result = block(connection)
        result
    }
}

interface DatabaseProviderContract {
    fun init()
    suspend fun <T> dbQuery(block: suspend (Connection) -> T): T
}