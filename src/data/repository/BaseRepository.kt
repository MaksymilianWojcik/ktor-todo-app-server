package com.mwcode.server.data.repository

import com.mwcode.server.config.DatabaseProvider
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

abstract class BaseRepository : KoinComponent {

    private val dbProvider by inject<DatabaseProvider>()

    suspend fun <T> dbQuery(block: () -> T): T = dbProvider.dbQuery(block)
}
