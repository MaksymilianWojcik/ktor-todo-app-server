package com.mwcode.server.data.entity

data class TaskEntity(
    val id: Int,
    val name: String,
    val description: String,
    val isActive: Boolean,
    val ownerId: Int
)
