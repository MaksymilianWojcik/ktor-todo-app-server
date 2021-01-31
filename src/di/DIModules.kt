package com.mwcode.server.di

import com.mwcode.server.config.DatabaseProvider
import com.mwcode.server.data.dao.AccountDao
import com.mwcode.server.data.dao.Accounts
import com.mwcode.server.data.dao.TaskDao
import com.mwcode.server.data.dao.Tasks
import com.mwcode.server.data.repository.AuthRepository
import com.mwcode.server.data.repository.TaskRepository
import com.mwcode.server.domain.controller.AuthController
import com.mwcode.server.domain.controller.TaskController
import org.koin.dsl.module.module

// Normally I'd put those in seperate files, like RepositoryModule, DaoModule or AuthModule, TaskModule etc.
object DIModules {
    val modules = module {
        single { TaskRepository(get()) }
        single { AuthRepository(get()) }
        single<AccountDao> { Accounts }
        single { DatabaseProvider() }
        single<TaskDao> { Tasks }
        single { AuthController(get()) }
        single { TaskController(get()) }
    }
}
