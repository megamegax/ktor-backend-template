package hu.hanprog.backend.api

import hu.hanprog.backend.api.user.UserApi
import hu.hanprog.backend.api.user.UserApiImpl
import org.koin.dsl.module

object ApiInjection {
    val koinBeans = module {
        single<UserApi> { UserApiImpl }
    }
}