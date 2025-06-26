package com.example.minexp.data.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "note_cards")
data class NoteCard(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") // Explicit column name
    val id: Long = 0L,

    @ColumnInfo(name = "title") // Explicit column name
    val title: String,

    @ColumnInfo(name = "content") // Explicit column name
    val content: String,

    @ColumnInfo(name = "timestamp") // Explicit column name
    val timestamp: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "created_at") // PASTIKAN INI ADA
    val createdAt: Long = System.currentTimeMillis()
)