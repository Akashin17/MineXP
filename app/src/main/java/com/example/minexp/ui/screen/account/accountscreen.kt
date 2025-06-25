package com.example.minexp.ui.screen.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
// import androidx.compose.foundation.layout.Spacer // Tidak digunakan secara langsung di sini
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
// import androidx.compose.foundation.layout.height // Tidak digunakan secara langsung di sini
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever // Untuk Reset Data
import androidx.compose.material.icons.filled.Info // Untuk Remove Account
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
// import androidx.compose.material3.Surface // Tidak digunakan secara langsung di sini
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// import androidx.compose.ui.graphics.Color // Tidak digunakan secara langsung di sini
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minexp.ui.viewmodel.QuestViewModel
import com.example.minexp.ui.viewmodel.NoteCardViewModel // Pastikan import ini benar
// Import ViewModel lain jika ada (misalnya, NoteViewModel, etc.)
// import com.example.minexp.ui.viewmodel.NoteViewModel
import kotlinx.coroutines.launch

@Composable
fun AccountScreenContent(
    // Inject kedua ViewModel
    questViewModel: QuestViewModel = viewModel(),
    noteCardViewModel: NoteCardViewModel = viewModel() // Inject NoteCardViewModel
    // Tambahkan ViewModel lain di sini jika diperlukan
    // , anotherViewModel: AnotherViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Gunakan padding dari Scaffold
                .padding(top = 16.dp, start = 16.dp, end = 16.dp) // Padding tambahan untuk konten
        ) {
            Text(
                "Account Settings",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp) // Padding internal Card
                ) {
                    AccountOptionItem(
                        text = "Reset All Data",
                        icon = Icons.Filled.DeleteForever,
                        onClick = {
                            showResetDialog = true // Tampilkan dialog konfirmasi
                        }
                    )

                    AccountOptionItem(
                        text = "Remove Account",
                        icon = Icons.Filled.Info,
                        onClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "This feature is not yet available.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                }
            }
        }

        // Dialog Konfirmasi Reset Data
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text("Confirm Reset") },
                text = { Text("Are you sure you want to reset all your data? This action cannot be undone.") },
                confirmButton = {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                // Panggil fungsi clear untuk semua ViewModel yang relevan
                                questViewModel.clearAllQuests()
                                noteCardViewModel.clearAllNoteCards() // Pastikan nama fungsi ini benar di ViewModel Anda
                                // Panggil fungsi clear untuk ViewModel lain di sini
                                // anotherViewModel.clearAllData()
                                // ...
                                snackbarHostState.showSnackbar(
                                    message = "All data has been reset.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            showResetDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Reset", color = MaterialTheme.colorScheme.onError)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun AccountOptionItem(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp), // Padding untuk setiap item
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}