package com.mwcode.server.domain.routes

import com.mwcode.server.ROUTE_API
import com.mwcode.server.domain.model.LoginRequest
import com.mwcode.server.domain.model.RegisterRequest
import com.mwcode.server.domain.controller.AuthController
import com.mwcode.server.domain.errors.ERROR_CODE_INVALID_ARGUMENTS
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

const val ENDPOINT_AUTHENTICATION = "/authentication"
const val ENDPOINT_LOGIN = "/login"
const val ENDPOINT_REGISTER = "/register"
const val ENDPOINT_REGISTER_LOGIN = "/registerAndLogin"
const val ENDPOINT_TOKEN = "/token"
const val ENDPOINT_LOGOUT = "/logout"

fun Routing.authenticationRoutes() {

    val authController by inject<AuthController>()

    route(ROUTE_API) {
        route(ENDPOINT_AUTHENTICATION) {

            post(ENDPOINT_LOGIN) {
                val credentials = call.receiveOrNull<LoginRequest>()
                credentials?.let {
                    val token = authController.login(credentials)
                    call.respond(HttpStatusCode.OK, token)
                } ?: call.respond(HttpStatusCode.BadRequest, ERROR_CODE_INVALID_ARGUMENTS)
            }

            post(ENDPOINT_REGISTER_LOGIN) {
                runCatching {
                    call.receive<RegisterRequest>()
                }.onSuccess { data ->
                    val token = authController.registerAndLogin(data)
                    call.respond(HttpStatusCode.Accepted, token)
                }.onFailure {
                    call.respond(HttpStatusCode.BadRequest, ERROR_CODE_INVALID_ARGUMENTS)
                }
            }

            post(ENDPOINT_REGISTER) {
                val requestData = call.receive<RegisterRequest>()
                val token = authController.register(requestData)
                call.respond(HttpStatusCode.OK, token)
            }

            authenticate {
                post(ENDPOINT_LOGOUT) {
                    val accountId = call.user.id
                    authController.logout(accountId)?.let {
                        call.respond(HttpStatusCode.OK, it)
                    } ?: call.respond(HttpStatusCode.BadRequest, ERROR_CODE_INVALID_ARGUMENTS)
                }
                post(ENDPOINT_TOKEN) {
                    // TODO: refresh token
                }
            }
        }
    }
}

fun Application.authenticationRoutes() = routing {
    authenticationRoutes()
}
