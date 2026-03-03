package com.deyvieat.shoptogether.features.auth.data.datasources.remote.mapper

import com.deyvieat.shoptogether.features.auth.data.datasources.remote.models.AuthResponseDto
import com.deyvieat.shoptogether.features.auth.domain.entities.AuthResult

fun AuthResponseDto.toDomain() = AuthResult(
    userId = user.id,
    name = user.name,
    email = user.email,
    token = token
)