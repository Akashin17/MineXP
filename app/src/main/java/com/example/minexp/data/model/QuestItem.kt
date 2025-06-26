package com.example.minexp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "quest_items")
data class QuestItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String, // Nama quest

    @ColumnInfo(name = "is_completed")
    var isCompleted: Boolean = false, // Status apakah quest sudah selesai

    @ColumnInfo(name = "description") // Opsional: deskripsi quest
    var description: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)