package hu.hanprog.backend.database.dao

import hu.hanprog.backend.database.DatabaseProviderContract
import hu.hanprog.backend.dotenv
import hu.hanprog.backend.model.PostUserBody
import hu.hanprog.backend.model.PutUserBody
import hu.hanprog.backend.model.User
import kotlinx.coroutines.reactive.awaitSingle
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object Users : UserDao, KoinComponent {
    val databaseProvider by inject<DatabaseProviderContract>()

    override suspend fun getUserById(userId: Int): User {
        return databaseProvider.dbQuery { connection ->
            val preparedStatement = connection.createStatement("SELECT * FROM users WHERE id = ?")
            preparedStatement.bind(0, userId)
            val result = preparedStatement.execute().awaitSingle()
            result.map { row ->
                User(
                    id = row.get("id") as Int,
                    fullname = row.get("userfullname").toString(),
                    username = row.get("user").toString(),
                    password = row.get("password").toString()
                )
            }.awaitSingle()
        }
    }

    override suspend fun insertUser(postUser: PostUserBody): Int? {
        return databaseProvider.dbQuery { connection ->
            val preparedStatement =
                connection.createStatement("INSERT INTO users (userfullname, user, password) VALUES (?, ?, ?)")
            preparedStatement.bind(0, postUser.fullname)
            preparedStatement.bind(1, postUser.username)
            preparedStatement.bind(2, postUser.password)
            preparedStatement.returnGeneratedValues("id")
            val result = preparedStatement.execute().awaitSingle()
            result.map { row -> row.get("id") as Int }.awaitSingle()
        }
    }

    override suspend fun updateUser(userId: Int, putUser: PutUserBody): Unit {
        return databaseProvider.dbQuery { connection ->
            val preparedStatement =
                connection.createStatement("UPDATE users SET userfullname = ?, user = ? WHERE id = ?")
            preparedStatement.bind(0, putUser.fullname)
            preparedStatement.bind(1, putUser.username)
            preparedStatement.bind(2, userId)
            preparedStatement.execute().awaitSingle()
        }
    }


    override suspend fun deleteUser(userId: Int): Boolean {
        return databaseProvider.dbQuery { connection ->
            val preparedStatement =
                connection.createStatement("DELETE FROM users WHERE id = ?")
            preparedStatement.bind(0, userId)

            preparedStatement.execute().awaitSingle()

            true
        }

    }

    override suspend fun getUserByCredentials(username: String, password: String): User? {
        return databaseProvider.dbQuery { connection ->
            val preparedStatement =
                connection.createStatement("SELECT * FROM ${dotenv["DB_NAME"]}.users WHERE user = ? AND password = ?")
            preparedStatement.bind(0, username)
            preparedStatement.bind(1, password)
            try {
                val result = preparedStatement.execute().awaitSingle()
                result.map { row ->
                    User(
                        id = row.get("id") as Int,
                        fullname = row.get("userfullname").toString(),
                        username = row.get("user").toString(),
                        password = row.get("password").toString()
                    )
                }.awaitSingle()
            } catch (e: NoSuchElementException) {
                e.printStackTrace()
                null
            }
        }
    }
}


interface UserDao {
    suspend fun getUserById(userId: Int): User?
    suspend fun insertUser(postUser: PostUserBody): Int?
    suspend fun updateUser(userId: Int, putUser: PutUserBody)
    suspend fun deleteUser(userId: Int): Boolean
    suspend fun getUserByCredentials(username: String, password: String): User?
}