package com.example.minexp.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.minexp.ui.screen.account.AccountScreenContent
import com.example.minexp.ui.screen.homepage.HomeScreenContent
import com.example.minexp.ui.screen.ideaspace.IdeaSpaceScreenContent
import com.example.minexp.ui.screen.navigation.BottomNavigationBar
import com.example.minexp.ui.screen.noteedit.NoteScreenContent
import com.example.minexp.ui.screen.quest.QuestScreenContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Perbarui kondisi untuk showBottomBarAndFab agar lebih spesifik
    // jika rute "noteedit" memiliki argumen
    val showBottomBarAndFab = when {
        currentRoute?.startsWith("noteedit/") == true -> false // Untuk noteedit/{noteId}
        currentRoute == "noteedit" -> false // Jika Anda masih memiliki rute "noteedit" tanpa argumen (sebaiknya dihindari jika selalu butuh ID)
        else -> true
    }

    Scaffold(
        topBar = {
            // Logika TopAppBar Anda untuk "noteedit" sepertinya sudah benar
            // jika Anda ingin TopAppBar yang berbeda untuk layar edit.
            // Pastikan currentRoute cocok dengan rute yang Anda gunakan untuk NoteScreenContent.
            if (currentRoute?.startsWith("noteedit/") == true) {
                CenterAlignedTopAppBar(
                    title = {
                        Button(
                            onClick = { /* Aksi saat chip diklik */ },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66D9E8)),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                        ) {
                            Text("Uncategorized", color = Color.Black, fontSize = 14.sp)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Undo */ }) {
                            Icon(Icons.Default.Undo, "Undo", tint = Color.White)
                        }
                        IconButton(onClick = { /* Redo */ }) {
                            Icon(Icons.Default.Redo, "Redo", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFF8E63D4) // Warna TopAppBar untuk noteedit
                    )
                )
            }
        },
        bottomBar = {
            if (showBottomBarAndFab) {
                BottomNavigationBar(navController = navController, currentRoute = currentRoute)
            }
        },
        floatingActionButton = {
            if (showBottomBarAndFab) {
                FloatingActionButton(
                    onClick = {
                        // Navigasi ke layar edit untuk CATATAN BARU.
                        // Kirim ID khusus (misalnya -1L) yang akan diinterpretasikan sebagai null (catatan baru)
                        // oleh NoteScreenContent.
                        navController.navigate("noteedit/-1L")
                    },
                    shape = CircleShape,
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    modifier = Modifier.offset(y = 35.dp)
                ) {
                    Icon(Icons.Default.Add, "Add Note")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        // Sesuaikan containerColor berdasarkan rute yang benar
        containerColor = if (currentRoute?.startsWith("noteedit/") == true) Color.White else Color(0xFFF8F8F8)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home", // Atau rute awal Anda
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreenContent(/* ... parameter jika ada ... */) }
            composable("quest") { QuestScreenContent(/* ... parameter jika ada ... */) }
            composable("ideaspace") {
                IdeaSpaceScreenContent(
                    onNavigateToEditNote = { noteId ->
                        navController.navigate("noteedit/$noteId") // Navigasi ke rute edit dengan ID
                    }
                )
            }
            composable("account") { AccountScreenContent(/* ... parameter jika ada ... */) }

            // Definisikan rute untuk noteedit dengan argumen noteId
            composable(
                route = "noteedit/{noteId}",
                arguments = listOf(navArgument("noteId") { type = NavType.LongType })
            ) { backStackEntry ->
                val noteIdArg = backStackEntry.arguments?.getLong("noteId")
                // Jika noteIdArg adalah -1L (atau nilai default Anda), berarti ini catatan baru,
                // sehingga teruskan null ke NoteScreenContent.
                val actualNoteId = if (noteIdArg == -1L) null else noteIdArg

                NoteScreenContent(
                    noteId = actualNoteId,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                    // viewModel akan disediakan secara default oleh viewModel() di dalam NoteScreenContent
                )
            }
        }
    }
}