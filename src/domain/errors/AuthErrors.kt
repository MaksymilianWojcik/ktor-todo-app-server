package com.mwcode.server.domain.errors

import com.mwcode.server.domain.model.ErrorResponseWrapper
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

const val ERROR_CODE_INVALID_USER = "Invalid.User"
const val ERROR_CODE_USER_REGISTERED = "User.Registered"
const val ERROR_CODE_INVALID_TOKEN = "Invalid.Token"

fun StatusPages.Configuration.authenticationStatusPages() {
    exception<AuthenticationException> { cause ->
        call.respond(HttpStatusCode.Unauthorized, ErrorResponseWrapper(HttpStatusCode.Unauthorized.value, cause.message))
    }
    exception<AuthorizationException> { cause ->
        call.respond(HttpStatusCode.Forbidden, ErrorResponseWrapper(HttpStatusCode.Forbidden.value, cause.message))
    }
    exception<InvalidUserException> { cause ->
        call.respond(HttpStatusCode.BadRequest, ErrorResponseWrapper(HttpStatusCode.BadRequest.value, cause.message))
    }
}

// Auth
class AuthenticationException(override val message: String = ERROR_CODE_INVALID_USER) : RuntimeException()
class AuthorizationException(override val message: String = ERROR_CODE_INVALID_TOKEN) : RuntimeException()
class InvalidUserException(override val message: String = ERROR_CODE_INVALID_USER) : RuntimeException()
