package com.deyvieat.shoptogether.features.rooms.domain.entities

data class AuctionRoom(val id: String,
                       val name: String,
                       val description: String,
                       val createdBy: String,
                       val isActive: Boolean,
                       val createdAt: String)
