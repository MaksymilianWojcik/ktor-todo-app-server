package com.mwcode.server.utils

inline fun <T: Any, R> whenNotNull(input: T?, cb: (T) -> R): R? = input?.let(cb)
