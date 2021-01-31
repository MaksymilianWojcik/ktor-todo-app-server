package com.mwcode.server.domain.model

import io.ktor.auth.*
import kotlinx.serialization.Serializable

// TODO: Separate models: AccountRequest, AccountResponse and same for all
@Serializable
data class Account(
    val id: Int,
    val username: String,
    val email: String,
    val lastLogin: String? = null,
    val isLoggedIn: Boolean,
    val createdAt: String
) : Principal
