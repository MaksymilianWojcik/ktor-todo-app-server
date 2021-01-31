package com.mwcode.server.domain.errors

import com.mwcode.server.domain.model.ErrorResponseWrapper
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

const val ERROR_CODE_TASK_NOT_FOUND = "Task.NotFound"

fun StatusPages.Configuration.taskStatusPages() {
    exception<TaskNotFoundException> { cause ->
        call.respond(HttpStatusCode.NotFound, ErrorResponseWrapper(HttpStatusCode.NotFound.value, cause.message))
    }
}

class TaskNotFoundException(override val message: String = ERROR_CODE_TASK_NOT_FOUND) : RuntimeException()
