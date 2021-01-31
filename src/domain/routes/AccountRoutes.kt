package com.mwcode.server.domain.routes

import com.mwcode.server.ROUTE_API
import com.mwcode.server.domain.model.Account
import com.mwcode.server.domain.model.ResponseWrapper
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

private const val ENDPOINT_ME = "/me"

fun Routing.me() {
    route(ROUTE_API) {
        authenticate(optional = false) {
            get(ENDPOINT_ME) {
                val response = ResponseWrapper<Account>(data = call.user)
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}

val ApplicationCall.user get() = authentication.principal<Account>()!!

fun Application.accountRoutes() = routing { me() }
