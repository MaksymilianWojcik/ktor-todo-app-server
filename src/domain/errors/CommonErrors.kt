package com.mwcode.server.domain.errors

import com.mwcode.server.domain.model.ErrorResponseWrapper
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

const val ERROR_CODE_SERVER_ERROR = "Server.Error"
const val ERROR_CODE_INVALID_ARGUMENTS = "Invalid.Arguments"
const val ERROR_CODE_MISSING_ARGUMENTS = "Missing.Arguments"

fun StatusPages.Configuration.commonStatusPages() {
    exception<MissingArgumentException> { cause ->
        call.respond(HttpStatusCode.BadRequest, ErrorResponseWrapper(HttpStatusCode.BadRequest.value, cause.message))
    }
    exception<UnsupportedMediaTypeException> { cause ->
        call.respond(HttpStatusCode.BadRequest, ErrorResponseWrapper(HttpStatusCode.BadRequest.value, cause.message))
    }
    exception<UnknownError> { cause ->
        call.respond(HttpStatusCode.InternalServerError, ErrorResponseWrapper(HttpStatusCode.InternalServerError.value, cause.message))
    }
    exception<NotFoundException> { cause ->
        call.respond(HttpStatusCode.NotFound, ErrorResponseWrapper(HttpStatusCode.NotFound.value, cause.message))
    }
    exception<ContentTransformationException> { cause ->
        call.respond(HttpStatusCode.BadRequest, ErrorResponseWrapper(HttpStatusCode.BadRequest.value, cause.message))
    }
    exception<Throwable> { cause ->
        // Added this to handle kotlin serialization error like missing field
        when (cause) {
            is kotlinx.serialization.SerializationException -> call.respond(HttpStatusCode.BadRequest, ErrorResponseWrapper(HttpStatusCode.BadRequest.value, cause.message))
            else -> call.respond(HttpStatusCode.InternalServerError, ErrorResponseWrapper(HttpStatusCode.InternalServerError.value, cause.message))
        }
    }
}

// Common
data class MissingArgumentException(override val message: String = ERROR_CODE_MISSING_ARGUMENTS) : Exception()
data class InvalidArgumentException(override val message: String = ERROR_CODE_INVALID_ARGUMENTS) : Exception()
