package com.deyvieat.shoptogether.features.votes.data.datasources.local.dao

import androidx.room.*
import com.deyvieat.shoptogether.features.votes.data.datasources.local.entities.VoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VoteDao {
    @Query("SELECT * FROM votes WHERE productId = :productId")
    fun observeVotes(productId: String): Flow<List<VoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vote: VoteEntity)

    @Query("DELETE FROM votes WHERE id = :id")
    suspend fun delete(id: String)
}
