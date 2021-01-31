package com.mwcode.server.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseWrapper<T>(
    val count: Int? = null,
    val pages: Int? = null,
    val data: T
)