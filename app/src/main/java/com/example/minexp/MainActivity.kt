package com.example.minexp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.minexp.ui.screen.account.AccountScreen
import com.example.minexp.ui.screen.homepage.HomeScreen
import com.example.minexp.ui.screen.ideaspace.IdeaSpaceScreen
import com.example.minexp.ui.screen.quest.QuestScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Navigation()
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                navController = navController,
                currentRoute = currentRoute
            )
        }
        composable("quest") {
            QuestScreen(
                navController = navController,
                currentRoute = currentRoute
            )
        }
        composable("ideaspace") {
            IdeaSpaceScreen(
                navController = navController,
                currentRoute = currentRoute
            )
        }
        composable("account") {
            AccountScreen(
                navController = navController,
                currentRoute = currentRoute
            )
        }
    }
}
