package com.example.minexp.ui.screen.noteedit

import android.widget.Toast // Import untuk Toast jika ingin digunakan
import androidx.compose.animation.core.copy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete // Opsional: untuk tombol hapus
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minexp.data.model.NoteCard
import com.example.minexp.ui.viewmodel.NoteCardViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenContent(
    noteId: Long?,
    noteCardViewModel: NoteCardViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    // Opsional: Tambahkan callback untuk menghapus jika Anda ingin tombol hapus di layar ini
    // onNavigateBackAfterDelete: () -> Unit = onNavigateBack // Defaultnya sama dengan onNavigateBack
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // State untuk judul dan konten, diinisialisasi sekali
    var title by remember(noteId) { mutableStateOf("") }
    var content by remember(noteId) { mutableStateOf("") }
    // State untuk menandai apakah ini catatan baru atau bukan
    val isNewNote = noteId == null
    // State untuk menyimpan referensi ke catatan yang sedang diedit (jika ada)
    var currentNote by remember { mutableStateOf<NoteCard?>(null) }
    // State untuk menandai apakah data awal sudah dimuat
    var initialDataLoaded by remember(noteId) { mutableStateOf(false) }

    // LaunchedEffect untuk mengambil data catatan jika noteId ada (mode edit)
    // dan hanya dijalankan sekali saat noteId berubah atau saat pertama kali komposisi
    LaunchedEffect(key1 = noteId, key2 = initialDataLoaded) {
        if (!isNewNote && !initialDataLoaded) {
            noteCardViewModel.getNoteById(noteId!!).collectLatest { note -> // noteId!! aman karena !isNewNote
                currentNote = note
                title = note.title ?: ""
                content = note.content ?: ""
                initialDataLoaded = true // Tandai bahwa data awal sudah dimuat
            }
        } else if (isNewNote) {
            // Reset state untuk catatan baru jika pengguna kembali ke layar ini untuk membuat catatan baru
            // setelah sebelumnya mengedit.
            title = ""
            content = ""
            currentNote = null
            initialDataLoaded = true // Untuk catatan baru, anggap data "awal" (kosong) sudah siap
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isNewNote) "Add Note" else "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Opsional: Tombol Hapus jika bukan catatan baru
                    if (!isNewNote && currentNote != null) {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                currentNote?.let { noteToDelete ->
                                    noteCardViewModel.delete(noteToDelete)
                                    // Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
                                    onNavigateBack() // Atau onNavigateBackAfterDelete jika berbeda
                                }
                            }
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete Note")
                        }
                    }
                    IconButton(onClick = {
                        if (title.isBlank() && content.isBlank()) {
                            Toast.makeText(context, "Title and content cannot be empty", Toast.LENGTH_SHORT).show()
                            return@IconButton
                        }

                        coroutineScope.launch {
                            val noteToSave: NoteCard
                            if (isNewNote) {
                                noteToSave = NoteCard(title = title.trim(), content = content.trim())
                                noteCardViewModel.insert(noteToSave)
                            } else {
                                currentNote?.let { existingNote ->
                                    noteToSave = existingNote.copy(title = title.trim(), content = content.trim())
                                    noteCardViewModel.update(noteToSave)
                                }
                            }
                            onNavigateBack()
                        }
                    }) {
                        Icon(Icons.Filled.Done, contentDescription = "Save Note")
                    }
                }
            )
        }
    ) { paddingValues ->
        // Tambahkan verticalScroll jika konten bisa lebih panjang dari layar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()) // Memungkinkan scroll jika konten panjang
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle( // Gunakan TextStyle langsung
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                singleLine = true // Biasanya judul satu baris
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 150.dp), // Beri tinggi minimal untuk konten
                textStyle = TextStyle(fontSize = 16.sp),
            )
        }
    }
}