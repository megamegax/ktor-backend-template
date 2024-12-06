package hu.hanprog.backend.database.injection

import hu.hanprog.backend.database.dao.UserDao
import hu.hanprog.backend.database.dao.Users
import org.koin.dsl.module


object DaoInjection {
    val koinBeans = module {
        single<UserDao> { Users }
    }
}