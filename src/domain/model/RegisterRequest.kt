package com.mwcode.server.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val username: String,
    val firstName: String,
    val surname: String
)
