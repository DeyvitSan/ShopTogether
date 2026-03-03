package com.deyvieat.shoptogether.features.auth.domain.entities

data class AuthResult(
    val userId: String,
    val name: String,
    val email: String,
    val token: String? = null
)
