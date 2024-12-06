package hu.hanprog.backend.modules.injection

import hu.hanprog.backend.modules.user.UserController
import hu.hanprog.backend.modules.user.UserControllerImp
import org.koin.dsl.module

object ControllersInjection {
    val koinBeans = module {
        single<UserController> { UserControllerImp() }
    }
}