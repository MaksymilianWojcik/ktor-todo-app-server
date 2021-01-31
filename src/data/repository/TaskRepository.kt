package com.mwcode.server.data.repository

import com.mwcode.server.data.dao.TaskDao
import com.mwcode.server.data.entity.TaskEntity
import com.mwcode.server.domain.errors.ERROR_CODE_SERVER_ERROR
import com.mwcode.server.domain.errors.TaskNotFoundException
import org.koin.standalone.KoinComponent

class TaskRepository(
    private val taskDao: TaskDao
) : BaseRepository(), KoinComponent {

    suspend fun getTaskById(id: Int): TaskEntity = dbQuery {
        taskDao.getTaskById(id)
    } ?: throw TaskNotFoundException()

    suspend fun getAllTasks(): List<TaskEntity> = dbQuery { taskDao.getAllTasks() }

    suspend fun getTasksForAccount(accountId: Int): List<TaskEntity> = dbQuery { taskDao.getTasksForAccount(accountId) }

    suspend fun createTask(task: TaskEntity): TaskEntity = dbQuery {
        taskDao.createTask(task)  ?: throw UnknownError(ERROR_CODE_SERVER_ERROR)
    }

    suspend fun updateTask(task: TaskEntity): TaskEntity = dbQuery {
        taskDao.updateTask(task)
    } ?: throw UnknownError(ERROR_CODE_SERVER_ERROR)

    suspend fun deleteTask(taskId: Int): Boolean = dbQuery {
        taskDao.deleteTask(taskId)
    }

}
