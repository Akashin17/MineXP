package com.example.minexp.ui.screen.quest

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Pastikan import ini ada
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minexp.data.model.QuestItem
import com.example.minexp.ui.viewmodel.QuestViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestScreenContent(
    questViewModel: QuestViewModel = viewModel()
) {
    val quests by questViewModel.allQuests.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var currentQuestToEdit by remember { mutableStateOf<QuestItem?>(null) } // Untuk membedakan mode Add/Edit
    var questNameInput by remember { mutableStateOf("") }
    var questDescriptionInput by remember { mutableStateOf("") }

    // Fungsi untuk menampilkan dialog tambah/edit
    fun openQuestDialog(quest: QuestItem? = null) {
        currentQuestToEdit = quest
        if (quest != null) {
            // Mode Edit
            questNameInput = quest.name
            questDescriptionInput = quest.description ?: ""
        } else {
            // Mode Tambah
            questNameInput = ""
            questDescriptionInput = ""
        }
        showDialog = true
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { openQuestDialog(null) }) { // Panggil dengan null untuk mode Tambah
                Icon(Icons.Filled.Add, contentDescription = "Add Quest")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Gunakan padding dari Scaffold
                .padding(16.dp), // Padding tambahan untuk konten
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "My Quests",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (quests.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No quests yet. Add some!")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(quests, key = { it.id }) { quest ->
                        QuestRow(
                            questItem = quest,
                            onToggleComplete = { questViewModel.toggleQuestCompleted(quest) },
                            onEdit = { openQuestDialog(quest) },
                            onDelete = { questViewModel.delete(quest) }
                        )
                    }
                }
            }
        }

        // Dialog untuk Tambah/Edit Quest
        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (currentQuestToEdit == null) "Add New Quest" else "Edit Quest",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        OutlinedTextField(
                            value = questNameInput,
                            onValueChange = { questNameInput = it },
                            label = { Text("Quest Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = questDescriptionInput,
                            onValueChange = { questDescriptionInput = it },
                            label = { Text("Description (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Cancel")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                if (questNameInput.isNotBlank()) {
                                    coroutineScope.launch {
                                        if (currentQuestToEdit == null) {
                                            // Tambah baru
                                            questViewModel.insert(
                                                QuestItem(
                                                    name = questNameInput.trim(),
                                                    description = questDescriptionInput.trim().ifEmpty { null }
                                                )
                                            )
                                        } else {
                                            // Edit yang sudah ada
                                            val updatedQuest = currentQuestToEdit!!.copy(
                                                name = questNameInput.trim(),
                                                description = questDescriptionInput.trim().ifEmpty { null }
                                            )
                                            questViewModel.update(updatedQuest)
                                        }
                                    }
                                    showDialog = false
                                }
                                // Opsional: Tambahkan validasi atau pesan error jika nama kosong
                            }) {
                                Text("Save")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuestRow(
    questItem: QuestItem,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        // Anda bisa menambahkan onClick pada Card jika ingin aksi lain saat seluruh baris diklik
        // .clickable { /* Aksi jika seluruh card diklik */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp), // Padding yang cukup
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = questItem.isCompleted,
                onCheckedChange = { onToggleComplete() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) { // Column ini mengambil sisa ruang
                Text(
                    text = questItem.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (questItem.isCompleted) TextDecoration.LineThrough else null,
                    color = if (questItem.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface // Warna teks sesuai status
                )
                if (!questItem.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = questItem.description!!, // Aman karena sudah dicek isNullOrBlank
                        fontSize = 14.sp,
                        color = if (questItem.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurfaceVariant, // Warna untuk deskripsi
                        textDecoration = if (questItem.isCompleted) TextDecoration.LineThrough else null
                    )
                }
            }
            // Tombol Aksi di sebelah kanan
            Spacer(modifier = Modifier.width(8.dp)) // Jarak sebelum tombol
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit Quest",
                    tint = MaterialTheme.colorScheme.primary // Warna ikon
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Quest",
                    tint = MaterialTheme.colorScheme.error // Warna ikon untuk aksi destruktif
                )
            }
        }
    }
}