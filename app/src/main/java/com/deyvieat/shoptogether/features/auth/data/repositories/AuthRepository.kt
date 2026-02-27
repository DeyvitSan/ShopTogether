package com.deyvieat.shoptogether.features.auth.data.repositories

import android.media.session.MediaSessionManager
import com.deyvieat.shoptogether.core.di.SessionManager
import com.deyvieat.shoptogether.features.auth.data.datasources.remote.api.AuthApi
import com.deyvieat.shoptogether.features.auth.data.datasources.remote.mapper.toDomain
import com.deyvieat.shoptogether.features.auth.data.datasources.remote.models.LoginRequesDto
import com.deyvieat.shoptogether.features.auth.data.datasources.remote.models.RegisterRequestDto
import com.deyvieat.shoptogether.features.auth.data.datasources.remote.models.UpdateUserRequestDto
import com.deyvieat.shoptogether.features.auth.domain.entities.AuthResult
import com.deyvieat.shoptogether.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,

    private val session: MediaSessionManager
): AuthRepository {

    override suspend fun register(name: String, email: String, password: String): Result<AuthResult> =

        runCatching {
            val response = api.register(RegisterRequestDto(name, email, password)).toDomain()
            session.saveSession(response.token, response.userId, response.name)
            response
        }

    override suspend fun login(email: String, password: String): Result<AuthResult> =
        runCatching {
            val response = api.login(LoginRequesDto(email, password)).toDomain()
            session.saveSession(response.token,response.userId, response.name)
            response
        }

    override suspend fun updateUser(id: String, name: String, email: String): Result<Unit> =
        runCatching { api.updateUser(id,UpdateUserRequestDto(name,email)); Unit }

    override suspend fun deleteUser(id: String): Result<Unit> =
        runCatching { api.deleteUser(id); Unit }

    override suspend fun logout() = session.clearSession()
}