package hu.hanprog.backend.modules.user

import hu.hanprog.backend.api.user.UserApi
import hu.hanprog.backend.model.PutUserBody
import hu.hanprog.backend.model.User
import hu.hanprog.backend.statuspages.InvalidUserException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class UserControllerImp : UserController, KoinComponent {
    private val userApi by inject<UserApi>()

    override suspend fun findUserByCredentials(
        username: String,
        password: String
    ): User? {
        return userApi.getUserByCredentials(username, password)
    }

    override suspend fun findUserById(userId: Int): User? {
        return userApi.getUserById(userId)
    }

    override suspend fun updateProfile(userId: Int, putUser: PutUserBody) {
        userApi.updateUser(userId, putUser) ?: throw InvalidUserException()
    }

    override suspend fun removeUser(userId: Int) {
        userApi.deleteUser(userId)
    }
}

interface UserController {
    suspend fun findUserByCredentials(username: String, password: String): User?
    suspend fun findUserById(userId: Int): User?
    suspend fun updateProfile(userId: Int, putUser: PutUserBody)
    suspend fun removeUser(userId: Int)
}