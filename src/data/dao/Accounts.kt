package com.mwcode.server.data.dao

import com.mwcode.server.data.entity.AccountsEntity
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime

private const val COLUMN_ID = "id"
private const val COLUMN_USERNAME = "username"
private const val COLUMN_EMAIL = "email"
private const val COLUMN_PASSWORD = "password"
private const val COLUMN_LAST_LOGIN = "last_login"
private const val COLUMN_LOGGED_IN = "logged_in"
private const val CREATED_AT = "created_at"

object Accounts : Table(), AccountDao {
    val id: Column<Int> = integer(COLUMN_ID).autoIncrement().primaryKey()
    val username = varchar(COLUMN_USERNAME, 64).uniqueIndex()
    val email = varchar(COLUMN_EMAIL, 128).uniqueIndex()
    val password = varchar(COLUMN_PASSWORD, 64)
    val lastLogin = datetime(COLUMN_LAST_LOGIN).nullable()
    val isLoggedIn = bool(COLUMN_LOGGED_IN)
    val createdAt = datetime(CREATED_AT)

    override fun getAccountById(accountId: Int): AccountsEntity? {
        return select { id eq accountId }
            .mapNotNull { it.mapRowToAccountsEntity() }
            .singleOrNull()
    }

    override fun getAccountByUsername(accountUsername: String): AccountsEntity? {
        return select { username eq accountUsername }
            .mapNotNull { it.mapRowToAccountsEntity() }
            .singleOrNull()
    }

    override fun insertAccount(postAccount: AccountsEntity): AccountsEntity? {
        val id = (insert {
            it[username] = postAccount.username
            it[email] = postAccount.email
            it[password] = postAccount.passwordHashed
            it[isLoggedIn] = postAccount.isLoggedIn
            if (postAccount.isLoggedIn) it[lastLogin] = DateTime.now()
            it[createdAt] = DateTime.now()
        })[id]
        return getAccountById(id)
    }

    override fun updateAccount(accountToUpdate: AccountsEntity): AccountsEntity? {
        update({ id eq accountToUpdate.id }) { account ->
            account[username] = accountToUpdate.username
            account[email] = accountToUpdate.email
            account[isLoggedIn] = accountToUpdate.isLoggedIn

        }
        return getAccountById(accountToUpdate.id)
    }

    override fun updateLoginStatus(accountId: Int, loggedIn: Boolean): AccountsEntity? {
        update({ id eq accountId }) { account ->
            account[lastLogin] = DateTime.now()
            account[isLoggedIn] = loggedIn
        }
        return getAccountById(accountId)
    }

    override fun updateUser(accountId: Int, postAccount: AccountsEntity): AccountsEntity? {
        update({ id eq accountId }) { account ->
            account[username] = postAccount.username
            account[email] = postAccount.email
            account[lastLogin] = DateTime.now()
        }
        return getAccountById(accountId)
    }

    override fun deleteAccount(accountId: Int): Boolean = deleteWhere { id eq accountId } > 0
}

fun ResultRow.mapRowToAccountsEntity() = AccountsEntity(
    id = this[Accounts.id],
    username = this[Accounts.username],
    email = this[Accounts.email],
    passwordHashed = this[Accounts.password],
    lastLogin = this[Accounts.lastLogin].toString(),
    isLoggedIn = this[Accounts.isLoggedIn],
    createdAt = this[Accounts.createdAt].toString()
)

interface AccountDao {
    fun getAccountById(accountId: Int): AccountsEntity?
    fun getAccountByUsername(accountUsername: String): AccountsEntity?
    fun insertAccount(postAccount: AccountsEntity): AccountsEntity?
    fun updateLoginStatus(accountId: Int, loggedIn: Boolean): AccountsEntity?
    fun updateAccount(accountToUpdate: AccountsEntity): AccountsEntity?
    fun updateUser(accountId: Int, postAccount: AccountsEntity): AccountsEntity?
    fun deleteAccount(accountId: Int): Boolean
}
