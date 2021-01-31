package com.mwcode.server.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseWrapper(
    val code: Int,
    val message: String?
)