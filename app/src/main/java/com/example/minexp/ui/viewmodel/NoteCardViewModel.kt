package com.example.minexp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.minexp.data.AppDatabase
import com.example.minexp.data.model.NoteCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class NoteCardViewModel(application: Application) : AndroidViewModel(application) {

    private val noteCardDao = AppDatabase.getDatabase(application).noteCardDao()

    val allNoteCards: LiveData<List<NoteCard>> = noteCardDao.getAllNoteCards().asLiveData()

    // Fungsi baru untuk mengambil satu NoteCard berdasarkan ID sebagai Flow
    // Menggunakan filterNotNull() agar Flow hanya memancarkan nilai non-null
    // Ini berguna agar UI tidak perlu menangani kasus null secara eksplisit saat mengumpulkan
    fun getNoteById(noteId: Long): Flow<NoteCard> = noteCardDao.getNoteCardByIdFlow(noteId).filterNotNull()

    // Alternatif jika Anda ingin LiveData dan menangani null di UI
    // fun getNoteByIdLiveData(noteId: Long): LiveData<NoteCard?> = noteCardDao.getNoteCardByIdLiveData(noteId)


    fun insert(noteCard: NoteCard) = viewModelScope.launch {
        noteCardDao.insertNoteCard(noteCard)
    }

    fun update(noteCard: NoteCard) = viewModelScope.launch {
        noteCardDao.updateNoteCard(noteCard)
    }

    fun delete(noteCard: NoteCard) = viewModelScope.launch {
        noteCardDao.deleteNoteCard(noteCard)
    }

    fun clearAllNoteCards() {
        viewModelScope.launch {
            noteCardDao.clearAllNoteCards() // Panggil fungsi DAO untuk menghapus semua
        }
    }
}