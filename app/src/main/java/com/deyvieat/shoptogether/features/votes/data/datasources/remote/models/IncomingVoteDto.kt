package com.deyvieat.shoptogether.features.votes.data.datasources.remote.models

data class IncomingVoteDto(
    val type: String, // NEW_VOTE, CONFIRMED, ERROR
    val id: String = "",
    val message: String? = null,
    val userId: String = "",
    val productId: String = "",
    val value: Double = 0.0
)
