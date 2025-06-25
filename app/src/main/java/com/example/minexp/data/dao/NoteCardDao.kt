package com.example.minexp.data.dao
import androidx.room.*
import com.example.minexp.data.model.NoteCard
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteCard(noteCard: NoteCard)

    @Update
    suspend fun updateNoteCard(noteCard: NoteCard)

    @Delete
    suspend fun deleteNoteCard(noteCard: NoteCard)

    @Query("SELECT * FROM note_cards ORDER BY timestamp DESC")
    fun getAllNoteCards(): Flow<List<NoteCard>> // Gunakan Flow untuk pembaruan data secara reaktif

    @Query("SELECT * FROM note_cards WHERE id = :id")
    suspend fun getNoteCardById(id: Int): NoteCard?

    @Query("SELECT * FROM note_cards WHERE id = :id")
    fun getNoteCardByIdFlow(id: Long): Flow<NoteCard?>

    @Query("DELETE FROM note_cards")
    suspend fun clearAllNoteCards()
}

