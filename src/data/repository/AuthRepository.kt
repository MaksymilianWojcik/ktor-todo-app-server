package com.mwcode.server.data.repository

import com.mwcode.server.config.PasswordConfig
import com.mwcode.server.data.dao.AccountDao
import com.mwcode.server.data.entity.AccountsEntity
import com.mwcode.server.domain.errors.*
import org.koin.standalone.KoinComponent

class AuthRepository(
    private val accountDao: AccountDao
) : BaseRepository(), KoinComponent {

    suspend fun login(username: String, password: String): AccountsEntity = dbQuery {
        accountDao.getAccountByUsername(username)?.let { account ->
            if (PasswordConfig.validatePassword(password, account.passwordHashed)) {
                accountDao.updateLoginStatus(account.id, true)
            } else throw AuthenticationException(ERROR_CODE_INVALID_USER)
        } ?: throw AuthenticationException(ERROR_CODE_INVALID_USER)
    }

    suspend fun logout(accountId: Int) = dbQuery {
        accountDao.updateLoginStatus(accountId, false)
    }

    suspend fun register(account: AccountsEntity): AccountsEntity = dbQuery {
        accountDao.getAccountByUsername(account.username)?.let {
            throw InvalidUserException(ERROR_CODE_USER_REGISTERED)
        }
        accountDao.insertAccount(account) ?: throw UnknownError(ERROR_CODE_SERVER_ERROR)
    }

    suspend fun registerAndLogin(credentials: AccountsEntity): AccountsEntity = dbQuery {
        accountDao.getAccountByUsername(credentials.username)?.let {
            throw InvalidUserException(ERROR_CODE_USER_REGISTERED)
        }
        val accountsEntity = credentials.copy(isLoggedIn = true)
        accountDao.insertAccount(accountsEntity) ?: throw UnknownError(ERROR_CODE_SERVER_ERROR)
    }
}
