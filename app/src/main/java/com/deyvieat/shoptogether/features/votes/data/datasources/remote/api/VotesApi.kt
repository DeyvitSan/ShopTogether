package com.deyvieat.shoptogether.features.votes.data.datasources.remote.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.*

interface VotesApi {
    @POST("votes")
    suspend fun vote(@Body body: CreateVoteDto): VoteResponseDto

    @GET("votes/{productId}")
    suspend fun getByProduct(@Path("productId") productId: String): List<VoteResponseDto>
}

data class CreateVoteDto(
    @SerializedName("userId") val userId: String,
    @SerializedName("productId") val productId: String,
    @SerializedName("value") val value: Double
)

data class VoteResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("productId") val productId: String,
    @SerializedName("value") val value: Double,
    @SerializedName("createdAt") val createdAt: String
)
