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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

    val showBottomBarAndFab = when (currentRoute) {
        "noteedit" -> false
        else -> true
    }

    Scaffold(
        topBar = {
            if (currentRoute == "noteedit") {
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
                        containerColor = Color(0xFF8E63D4)
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
                    onClick = { navController.navigate("noteedit") },
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
        containerColor = if (currentRoute == "noteedit") Color.White else Color(0xFFF8F8F8)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreenContent() }
            composable("quest") { QuestScreenContent() }
            composable("ideaspace") { IdeaSpaceScreenContent() }
            composable("account") { AccountScreenContent() }
            composable("noteedit") { NoteScreenContent() }
        }
    }
}