package com.deyvieat.shoptogether.features.auth.domain.repositories

import com.deyvieat.shoptogether.features.auth.domain.entities.AuthResult

interface AuthRepository{
    suspend fun register(name: String, email: String, password: String): Result<AuthResult>

    suspend fun login(email: String, password: String): Result<AuthResult>

    suspend fun updateUser(id: String, name: String, email: String): Result<Unit>

    suspend fun deleteUser(id: String): Result<Unit>

    suspend fun logout()
}