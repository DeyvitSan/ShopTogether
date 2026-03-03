package com.deyvieat.shoptogether.features.votes.data.datasources.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "votes")
data class VoteEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val productId: String,
    val value: Double,
    val isPending: Boolean = false
)
