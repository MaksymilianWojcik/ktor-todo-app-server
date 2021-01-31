package com.mwcode.server.domain.controller

import com.mwcode.server.data.repository.TaskRepository
import com.mwcode.server.domain.model.ResponseWrapper
import com.mwcode.server.domain.model.Task
import com.mwcode.server.utils.toDomain
import com.mwcode.server.utils.toEntity

class TaskController(
    private val taskRepository: TaskRepository
) {

    suspend fun getAllTasks(): ResponseWrapper<List<Task>> {
        val tasks = taskRepository.getAllTasks().map { it.toDomain() }
        return ResponseWrapper(data = tasks)
    }

    suspend fun getTasksForAccount(accountId: Int): ResponseWrapper<List<Task>> {
        val tasks = taskRepository.getTasksForAccount(accountId).map { it.toDomain() }
        return ResponseWrapper(data = tasks)
    }

    suspend fun getTaskById(taskId: Int): ResponseWrapper<Task> {
        val task = taskRepository.getTaskById(taskId).toDomain()
        return ResponseWrapper(data = task)
    }

    suspend fun createTask(task: Task, accountId: Int): Task {
        val taskToCreate = task.copy(ownerId = accountId)
        return taskRepository.createTask(taskToCreate.toEntity()).toDomain()
    }

    suspend fun deleteTask(taskId: Int): Boolean {
        return taskRepository.deleteTask(taskId)
    }

    suspend fun updateTask(newTask: Task): ResponseWrapper<Task> {
        val task = taskRepository.updateTask(newTask.toEntity()).toDomain()
        return ResponseWrapper(data = task)
    }
}