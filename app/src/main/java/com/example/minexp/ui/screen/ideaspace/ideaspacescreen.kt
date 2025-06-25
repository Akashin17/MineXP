package com.example.minexp.ui.screen.ideaspace

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Pastikan import ini ada
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState // Untuk LiveData
// import androidx.compose.runtime.collectAsState // Jika ViewModel menggunakan Flow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minexp.data.model.NoteCard // Import model NoteCard Anda
import com.example.minexp.ui.viewmodel.NoteCardViewModel // Import ViewModel Anda

// Asumsi Anda memiliki Composable seperti ini untuk menampilkan satu item
// Jika belum ada, Anda bisa membuatnya atau mengadaptasi dari layar lain.
@Composable
fun IdeaCardItem(noteCard: NoteCard, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp), // Sudut membulat Card
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Semua konten, termasuk tanggal, harus berada di dalam Column ini
        // agar mendapatkan padding yang sama dan menghormati bentuk Card.
        Column(
            modifier = Modifier
                .padding(16.dp) // Padding diterapkan ke semua anak Column ini
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp) // Opsional, untuk tinggi minimal Card
        ) {
            // Konten utama
            Text(
                text = noteCard.title ?: "No Title",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = noteCard.content ?: "No Content",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3 // Atau sesuai kebutuhan
            )

            // Spacer ini akan mengambil semua ruang vertikal yang tersisa DI DALAM Column,
            // mendorong Text tanggal ke bawah, tetapi masih di dalam area padding.
            Spacer(modifier = Modifier.weight(1f))

            // Anda mungkin tidak memerlukan Spacer tambahan di sini jika padding Column sudah cukup,
            // tapi bisa ditambahkan jika ingin jarak ekstra antara konten dan tanggal.
            // Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Created: ${
                    java.text.SimpleDateFormat("MMM d, HH:mm", java.util.Locale.getDefault())
                        .format(java.util.Date(noteCard.createdAt))
                }",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.align(Alignment.End) // Rata kanan di dalam Column
            )
        }
    }
}


@Composable
fun IdeaSpaceScreenContent(
    noteCardViewModel: NoteCardViewModel = viewModel(), // 1. Dapatkan instance ViewModel
    onNavigateToEditNote: (noteId: Long) -> Unit // Callback untuk navigasi ke layar edit
) {
    // 2. Amati data dari ViewModel
    // Jika allNoteCards adalah LiveData:
    val noteCards by noteCardViewModel.allNoteCards.observeAsState(initial = emptyList())
    // Jika allNoteCards adalah Flow (pastikan ViewModel Anda mengekspos Flow):
    // val noteCards by noteCardViewModel.allNoteCards.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        // Judul atau elemen UI lain untuk IdeaSpace bisa ditambahkan di sini
        Text(
            "My Ideas",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White, // Sesuai kode Anda sebelumnya
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp) // Sesuai kode Anda sebelumnya
        ) {
            if (noteCards.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No ideas captured yet. Add a new note!")
                }
            } else {
                // 3. Tampilkan daftar NoteCard
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp) // Jarak antar item
                ) {
                    items(noteCards) { noteCard ->
                        IdeaCardItem(
                            noteCard = noteCard,
                            onClick = {
                                onNavigateToEditNote(noteCard.id) // Navigasi ke edit saat item diklik
                            }
                        )
                        // Spacer(modifier = Modifier.height(12.dp)) // Tidak perlu jika menggunakan verticalArrangement
                    }
                }
            }
        }
    }
}