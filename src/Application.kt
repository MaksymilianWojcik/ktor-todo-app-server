package com.mwcode.server

import com.mwcode.server.config.DatabaseProvider
import com.mwcode.server.config.JwtConfig
import com.mwcode.server.config.JwtConfig.AUDIENCE
import com.mwcode.server.config.JwtConfig.JWT_CLAIM_ID
import com.mwcode.server.config.JwtConfig.JWT_REALM
import com.mwcode.server.data.dao.AccountDao
import com.mwcode.server.di.DIModules
import com.mwcode.server.domain.errors.*
import com.mwcode.server.domain.routes.accountRoutes
import com.mwcode.server.domain.routes.authenticationRoutes
import com.mwcode.server.domain.routes.taskRoutes
import com.mwcode.server.utils.toDomain
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.routing.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import org.koin.ktor.ext.installKoin

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

const val API_VERSION = "v1"
const val ROUTE_API = "/api/$API_VERSION"

@Suppress("unused")
fun Application.module() {

    println("Environment: $envKind")

    installKoin(
        list = listOf(DIModules.modules)
    )

    val dbProvider by inject<DatabaseProvider>()
    dbProvider.init()
    val accountsDao by inject<AccountDao>()

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(Authentication) {
        jwt {
            realm = JWT_REALM
            verifier(JwtConfig.verifier)
            challenge { _, _ -> // called if jwt authentication fails
                throw AuthorizationException(ERROR_CODE_INVALID_TOKEN)
            }
            validate {
                if (it.payload.audience.contains(AUDIENCE)) {
                    it.payload.getClaim(JWT_CLAIM_ID)?.asInt()?.let {  id ->
                        dbProvider.dbQuery { accountsDao.getAccountById(id)?.toDomain() }
                    }
                } else throw AuthorizationException(ERROR_CODE_INVALID_TOKEN)
            }
        }
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true // we dont want to print null when not set, look at ResponseWrapper
            encodeDefaults = false
        })
    }

    routing {
        install(StatusPages) {
            authenticationStatusPages()
            commonStatusPages()
            taskStatusPages()
        }
    }

    authenticationRoutes()
    taskRoutes()
    accountRoutes()
}

val Application.envKind get() = environment.config.property("ktor.environment").getString()
val Application.isDev get() = envKind == "dev"
val Application.isProd get() = envKind != "dev"
