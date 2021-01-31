package com.mwcode.server.config

import com.mwcode.server.data.dao.Accounts
import com.mwcode.server.data.dao.Tasks
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.standalone.KoinComponent

private const val ENV_DB_DRIVER = "JDBC_DRIVER"
private const val ENV_DB_URL = "JDBC_DATABASE_URL"
private const val ENV_DB_USER = "DB_USER"
private const val ENV_DB_PASSWORD = "DB_PASSWORD"

private const val MAXIMUM_POOL_SIZE = 3

class DatabaseProvider : KoinComponent {

    fun init() {
        Database.connect(hikari())
        transaction {
            transaction {
                SchemaUtils.create(Accounts)
                SchemaUtils.create(Tasks)
            }
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = System.getenv(ENV_DB_DRIVER)
        config.jdbcUrl = System.getenv(ENV_DB_URL)
        config.maximumPoolSize = MAXIMUM_POOL_SIZE
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        val user = System.getenv(ENV_DB_USER)
        if (user != null) {
            config.username = user
        }
        val password = System.getenv(ENV_DB_PASSWORD)
        if (password != null) {
            config.password = password
        }
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) { transaction { block() } }
}
