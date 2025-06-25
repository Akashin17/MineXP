package com.example.minexp.ui.screen.homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration // Import untuk text-decoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minexp.data.model.QuestItem // Import model QuestItem
import com.example.minexp.ui.viewmodel.QuestViewModel // Import QuestViewModel

@Composable
fun HomeScreenContent(
    questViewModel: QuestViewModel = viewModel()
) {
    val allQuests by questViewModel.allQuests.collectAsState(initial = emptyList())

    // Ambil 4 quest teratas yang BELUM SELESAI untuk ditampilkan di TODO.
    // Ketika sebuah quest dicentang, status 'isCompleted'-nya akan berubah,
    // dan UI di TodoListSection akan merefleksikannya (coretan, warna abu-abu).
    // Quest tersebut akan tetap ada di daftar ini sampai terjadi recomposition
    // yang menyebabkan 'todoItems' dievaluasi ulang (misalnya, jika 'allQuests' berubah
    // karena ada quest baru ditambahkan atau dihapus dari QuestScreen, atau jika Anda
    // menavigasi keluar dan kembali ke HomeScreen).
    val todoItems = remember(allQuests) {
        allQuests
            // .sortedBy { it.isCompleted } // Opsional: tampilkan yang belum selesai dulu
            .sortedBy { it.createdAt }    // Kemudian urutkan berdasarkan waktu pembuatan
            .take(4)                       // Ambil 4 teratas
    }

    // Jika Anda ingin quest yang sudah selesai tetap muncul di daftar TODO ini
    // (dengan gaya selesai) bahkan setelah dicentang, Anda bisa mengubah logika di atas menjadi:
    // val todoItems = remember(allQuests) {
    //     allQuests
    //         .sortedBy { it.isCompleted } // Selesaikan yang belum selesai dulu
    //         .thenBy { it.createdAt }    // Kemudian urutkan berdasarkan waktu
    //         .take(4)                    // Ambil 4 teratas
    // }
    // Pilihan ini akan membuat item yang dicentang tetap ada di daftar TODO
    // dan hanya berubah gayanya.

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp)
    ) {
        HeaderSection()
        Spacer(modifier = Modifier.height(16.dp))
        XPLevelCard()
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            TodoListSection(
                todoQuests = todoItems, // Kirim daftar yang sudah difilter atau diurutkan
                onQuestToggle = { questItem, isChecked ->
                    // ViewModel akan mengupdate quest di database.
                    // Perubahan pada 'allQuests' akan memicu recomposition jika diperlukan.
                    val updatedQuest = questItem.copy(isCompleted = isChecked)
                    questViewModel.update(updatedQuest)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            RecentActivityCard(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Hello, People!", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifikasi",
            modifier = Modifier
                .background(Color.LightGray, shape = CircleShape)
                .padding(8.dp)
        )
    }
}

@Composable
fun XPLevelCard() {
    Card(
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF8A64D6)),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Level 1", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text("49/50 XP to level 2", color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = 0.98f,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(6.dp),
                color = Color(0xFFB3E5FC),
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun TodoListSection(
    todoQuests: List<QuestItem>,
    onQuestToggle: (QuestItem, Boolean) -> Unit, // Callback dengan QuestItem dan status Boolean baru
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .heightIn(min = 200.dp, max = 280.dp) // Sesuaikan tinggi jika perlu
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("TODO :", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            if (todoQuests.isEmpty()) {
                Text(
                    "No pending quests for now! 🎉", // Pesan jika tidak ada quest
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                // Menggunakan Column biasa karena jumlah item terbatas (misalnya, maks 4)
                // Jika item bisa banyak dan perlu scroll, gunakan LazyColumn.
                todoQuests.forEach { quest ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onQuestToggle(quest, !quest.isCompleted) } // Klik pada baris untuk toggle
                    ) {
                        Checkbox(
                            checked = quest.isCompleted, // Status checkbox dari data quest
                            onCheckedChange = { isChecked ->
                                onQuestToggle(quest, isChecked) // Panggil callback saat status checkbox berubah
                            },
                            colors = CheckboxDefaults.colors( // Kustomisasi warna checkbox
                                checkedColor = MaterialTheme.colorScheme.primary,
                                uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                checkmarkColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = quest.name,
                            fontSize = 12.sp,
                            // Terapkan coretan dan ubah warna jika quest sudah selesai
                            textDecoration = if (quest.isCompleted) TextDecoration.LineThrough else null,
                            color = if (quest.isCompleted) Color.Gray else LocalContentColor.current
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecentActivityCard(modifier: Modifier = Modifier) { // modifier sekarang digunakan
    Card(
        shape = RoundedCornerShape(30.dp), // Konsisten dengan XPLevelCard atau bisa beda
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier // Gunakan modifier yang diteruskan dari parent
            .heightIn(min = 200.dp, max = 280.dp) // Sesuaikan tinggi jika perlu
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(), // Biarkan Column mengisi Card
            horizontalAlignment = Alignment.CenterHorizontally // Pusatkan konten jika diinginkan
        ) {
            // Placeholder Gambar
            Box(
                modifier = Modifier
                    .size(100.dp) // Ukuran gambar placeholder
                    .background(Color.LightGray, shape = RoundedCornerShape(20.dp))
                // Anda bisa menambahkan konten di dalam Box ini jika perlu, misalnya Icon
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Recent", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Minecraft", fontWeight = FontWeight.SemiBold) // Contoh teks
            Text(
                "Lorem ipsum dolor sit amet. first we mine, then we craft. lests mine...",
                fontSize = 12.sp,
                color = Color.Gray // Warna teks deskripsi
            )
        }
    }
}
