package com.mwcode.server.data.entity

data class AccountsEntity(
    val id: Int = 0,
    val username: String,
    val email: String,
    val passwordHashed: String,
    val lastLogin: String? = null,
    val isLoggedIn: Boolean = false,
    val createdAt: String = ""
)
