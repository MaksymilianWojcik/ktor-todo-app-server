package com.mwcode.server.data.dao

import com.mwcode.server.data.entity.TaskEntity
import org.jetbrains.exposed.sql.*

private const val COLUMN_ID = "id"
private const val COLUMN_NAME = "name"
private const val COLUMN_DESCRIPTION = "description"
private const val COLUMN_IS_ACTIVE = "active"
private const val COLUMN_OWNER_ID = "owner_id"

object Tasks : Table(), TaskDao {
    val id = integer(COLUMN_ID).autoIncrement().primaryKey()
    val name = varchar(COLUMN_NAME, 64)
    val description = varchar(COLUMN_DESCRIPTION, 1024)
    val isActive = bool(COLUMN_IS_ACTIVE).default(false)
//    val ownerId = reference("owner_id", Accounts.id)
    val ownerId = integer(COLUMN_OWNER_ID).references(Accounts.id)


    override fun getTaskById(taskId: Int): TaskEntity? {
        return select { id eq taskId }
            .mapNotNull { it.mapRowToTasksEntity() }
            .singleOrNull()
    }

    override fun updateTask(task: TaskEntity): TaskEntity? {
        update({ id eq task.id }) { oldTask ->
            oldTask[name] = task.name
            oldTask[description] = task.description
            oldTask[isActive] = task.isActive
        }
        return getTaskById(task.id)
    }

    override fun getAllTasks(): List<TaskEntity> = selectAll().mapNotNull { it.mapRowToTasksEntity() }

    override fun getTasksForAccount(userId: Int): List<TaskEntity> =
        select { ownerId eq userId }.mapNotNull { it.mapRowToTasksEntity() }

    override fun createTask(task: TaskEntity): TaskEntity? {
        val id = (insert {
            it[name] = task.name
            it[description] = task.description
            it[isActive] = task.isActive
            it[ownerId] = task.ownerId
        })[id]
        return getTaskById(id)
    }

    override fun deleteTask(taskId: Int): Boolean = deleteWhere { id eq taskId } > 0

    override fun deleteTasksForUser(accountId: Int): Boolean = deleteWhere { ownerId eq accountId } > 0
}

fun ResultRow.mapRowToTasksEntity() = TaskEntity(
    id = this[Tasks.id],
    name = this[Tasks.name],
    description = this[Tasks.description],
    isActive = this[Tasks.isActive],
    ownerId = this[Tasks.ownerId]
)

interface TaskDao {
    fun getTaskById(taskId: Int): TaskEntity?
    fun getAllTasks(): List<TaskEntity>
    fun createTask(task: TaskEntity): TaskEntity?
    fun getTasksForAccount(userId: Int): List<TaskEntity>
    fun deleteTask(taskId: Int): Boolean
    fun updateTask(task: TaskEntity): TaskEntity?
    fun deleteTasksForUser(accountId: Int): Boolean
}
