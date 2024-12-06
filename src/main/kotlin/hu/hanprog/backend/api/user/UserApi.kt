package hu.hanprog.backend.api.user

import hu.hanprog.backend.model.PostUserBody
import hu.hanprog.backend.model.PutUserBody
import hu.hanprog.backend.model.User

interface UserApi {
    suspend fun getUserByCredentials(username: String, password: String): User?
    suspend fun getUserById(id: Int): User?
    suspend fun updateUser(id: Int, user: PutUserBody)
    suspend fun deleteUser(id: Int): Boolean
    suspend fun createUser(user: PostUserBody): User
}