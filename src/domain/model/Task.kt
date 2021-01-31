package com.mwcode.server.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Int = 0,
    val name: String,
    val description: String,
    val isActive: Boolean,
    val ownerId: Int = 0
)
