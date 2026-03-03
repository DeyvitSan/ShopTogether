package com.deyvieat.shoptogether.features.auth.domain.usecases

import com.deyvieat.shoptogether.features.auth.domain.entities.AuthResult
import com.deyvieat.shoptogether.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<AuthResult> =
        repo.login(email, password)
}