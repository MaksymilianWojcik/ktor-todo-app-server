package com.mwcode.server.domain.routes

import com.mwcode.server.ROUTE_API
import com.mwcode.server.domain.model.Task
import com.mwcode.server.domain.controller.TaskController
import com.mwcode.server.domain.errors.ERROR_CODE_INVALID_ARGUMENTS
import com.mwcode.server.domain.errors.InvalidArgumentException
import com.mwcode.server.domain.errors.MissingArgumentException
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

private const val PARAM_TASK_ID = "id"
private const val PARAM_OWNER_ID = "accountId"

const val ENDPOINT_TASK = "/task"

fun Routing.taskRoutes() {

    val taskController by inject<TaskController>()

    route(ROUTE_API) {
        authenticate {
            route(ENDPOINT_TASK) {

                get {
                    call.parameters[PARAM_TASK_ID]?.let {
                        if (it.isEmpty()) throw MissingArgumentException()
                        it.toIntOrNull()?.let { id ->
                            call.respond(HttpStatusCode.OK, taskController.getTaskById(id))
                            return@get
                        } ?: throw InvalidArgumentException()
                    }
                    call.parameters[PARAM_OWNER_ID]?.let {
                        if (it.isEmpty()) throw MissingArgumentException()
                        it.toIntOrNull()?.let { id ->
                            call.respond(HttpStatusCode.OK, taskController.getTasksForAccount(id))
                            return@get
                        } ?: throw InvalidArgumentException()
                    }
                    call.respond(HttpStatusCode.OK, taskController.getAllTasks())
                }

                post {
                    val task = call.receiveOrNull<Task>()
                    task?.let {
                        val createdTask = taskController.createTask(it, call.user.id)
                        call.respond(HttpStatusCode.OK, createdTask)
                    } ?: call.respond(HttpStatusCode.BadRequest, ERROR_CODE_INVALID_ARGUMENTS)
                }

                // TODO: Not tested
                put {
                    call.receiveOrNull<Task>()?.let {
                        val response = taskController.updateTask(it)
                        call.respond(HttpStatusCode.OK, response)
                    } ?: call.respond(HttpStatusCode.BadRequest, ERROR_CODE_INVALID_ARGUMENTS)
                }

                // TODO: Not tested
                delete {
                    call.request.queryParameters[PARAM_TASK_ID]?.toIntOrNull()?.let { id ->
                        when (taskController.deleteTask(id)) {
                            true -> call.respond(HttpStatusCode.OK) // We could create a wrapper, like Result with Success / Failuree
                            false -> call.respond(HttpStatusCode.InternalServerError)
                        }
                    } ?: call.respond(HttpStatusCode.BadRequest, ERROR_CODE_INVALID_ARGUMENTS)
                }

                get("/{$PARAM_TASK_ID}") {
                    val id = call.parameters[PARAM_TASK_ID]?.toIntOrNull()
                    id?.let {
                        call.respond(HttpStatusCode.OK, taskController.getTaskById(id))
                    } ?: call.respond(HttpStatusCode.BadRequest, ERROR_CODE_INVALID_ARGUMENTS)
                }
            }
        }
    }
}

fun Application.taskRoutes() = routing { taskRoutes() }
