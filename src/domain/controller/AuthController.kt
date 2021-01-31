package com.mwcode.server.domain.controller

import com.mwcode.server.config.JwtConfig
import com.mwcode.server.data.repository.AuthRepository
import com.mwcode.server.domain.model.LoginRequest
import com.mwcode.server.domain.model.RegisterRequest
import com.mwcode.server.domain.model.TokenResponse
import com.mwcode.server.utils.toAccountsEntity
import com.mwcode.server.utils.toDomain

class AuthController(
    private val authRepository: AuthRepository
) {

    suspend fun login(credentials: LoginRequest): TokenResponse {
        val account = authRepository.login(credentials.username, credentials.password).toDomain()
        return TokenResponse(JwtConfig.makeToken(account))
    }

    suspend fun registerAndLogin(credentials: RegisterRequest): TokenResponse {
        val account = authRepository.registerAndLogin(credentials.toAccountsEntity()).toDomain()
        return TokenResponse(JwtConfig.makeToken(account))
    }

    suspend fun register(credentials: RegisterRequest): TokenResponse {
        val account = authRepository.register(credentials.toAccountsEntity()).toDomain()
        return TokenResponse(JwtConfig.makeToken(account))
    }

    suspend fun logout(accountId: Int) = authRepository.logout(accountId)?.toDomain()
}