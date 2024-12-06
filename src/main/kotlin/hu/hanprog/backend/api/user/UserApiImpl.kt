package hu.hanprog.backend.api.user

import hu.hanprog.backend.database.dao.UserDao
import hu.hanprog.backend.model.PostUserBody
import hu.hanprog.backend.model.PutUserBody
import hu.hanprog.backend.model.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object UserApiImpl : UserApi, KoinComponent {
    private val usersDao by inject<UserDao>()

    override suspend fun getUserByCredentials(username: String, password: String): User? {
        return usersDao.getUserByCredentials(username, password)
    }

    override suspend fun getUserById(id: Int): User? {
        return usersDao.getUserById(id)
    }

    override suspend fun updateUser(id: Int, user: PutUserBody) {
        usersDao.updateUser(id, user)
    }

    override suspend fun deleteUser(id: Int): Boolean {
        return usersDao.deleteUser(id)
    }

    override suspend fun createUser(user: PostUserBody): User {
        val id = usersDao.insertUser(user)
        return usersDao.getUserById(id!!)!!
    }
}