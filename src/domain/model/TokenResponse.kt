package com.mwcode.server.domain.model

import kotlinx.serialization.Serializable

typealias Token = String

@Serializable
data class TokenResponse(
    val token: Token
)
