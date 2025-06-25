package com.example.minexp.data.dao

import androidx.room.*
import com.example.minexp.data.model.QuestItem
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuest(questItem: QuestItem)

    @Update
    suspend fun updateQuest(questItem: QuestItem)

    @Delete
    suspend fun deleteQuest(questItem: QuestItem)

    @Query("SELECT * FROM quest_items ORDER BY created_at DESC")
    fun getAllQuests(): Flow<List<QuestItem>>

    @Query("SELECT * FROM quest_items WHERE id = :id")
    fun getQuestById(id: Long): Flow<QuestItem?>

    @Query("DELETE FROM quest_items")
    suspend fun clearAllQuests()
}