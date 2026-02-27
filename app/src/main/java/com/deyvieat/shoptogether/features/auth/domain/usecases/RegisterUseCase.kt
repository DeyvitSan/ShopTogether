package com.deyvieat.shoptogether.features.auth.domain.usecases

import android.provider.ContactsContract
import com.deyvieat.shoptogether.features.auth.domain.entities.AuthResult
import com.deyvieat.shoptogether.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repo: AuthRepository) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<AuthResult> =
        repo.register(name, email, password)
}