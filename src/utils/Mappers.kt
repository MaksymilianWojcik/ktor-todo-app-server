package com.mwcode.server.utils

import com.mwcode.server.config.PasswordConfig
import com.mwcode.server.data.entity.AccountsEntity
import com.mwcode.server.data.entity.TaskEntity
import com.mwcode.server.domain.model.Account
import com.mwcode.server.domain.model.Task
import com.mwcode.server.domain.model.RegisterRequest

//val accountResponseMapper: (Account) -> AccountResponse = { account ->
//    AccountResponse(
//        id = account.id,
//        username = account.username,
//        email = account.email,
//        lastLogin = account.lastLogin ?: "",
//        loggedIn = account.isLoggedIn,
//        createdAt = account.createdAt
//    )
//}
//

fun AccountsEntity.toDomain(): Account = Account(
    id = this.id,
    username = this.username,
    email = this.email,
    lastLogin = this.lastLogin.toString(),
    isLoggedIn = this.isLoggedIn,
    createdAt = this.createdAt
)

fun TaskEntity.toDomain() = Task(
    id = this.id,
    name = this.name,
    description = this.description,
    isActive = this.isActive,
    ownerId = this.ownerId
)

fun Task.toEntity() = TaskEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    isActive = this.isActive,
    ownerId = this.ownerId
)

// TODO: Rename Request to models, and ad Models to Task and Account
fun RegisterRequest.toAccountsEntity() = AccountsEntity(
    username = this.username,
    email = this.email,
    passwordHashed = PasswordConfig.encryptPassword(this.password)
)
