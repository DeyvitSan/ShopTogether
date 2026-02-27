package com.deyvieat.shoptogether.features.auth.domain.entities

data class AuthResult(val token: String, val userId: String, val name: String, val email: String)