package com.mwcode.server.domain.model

import kotlinx.serialization.Serializable

// TODO: Fix required id and ownerId in the post request for creating task
@Serializable
data class Task(
    val id: Int,
    val name: String,
    val description: String,
    val isActive: Boolean,
    val ownerId: Int = 0 // TODO: Temporary 0
)
