package com.deyvieat.shoptogether.features.votes.domain.entities

data class Vote(
    val id: String,
    val userId: String,
    val productId: String,
    val value: Double,
    val isPending: Boolean = false
)
