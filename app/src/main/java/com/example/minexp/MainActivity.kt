package com.example.minexp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.activity.ComponentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.minexp.ui.screen.account.AccountScreen
import com.example.minexp.ui.screen.homepage.HomeScreen
import com.example.minexp.ui.screen.ideaspace.IdeaSpaceScreen
import com.example.minexp.ui.screen.quest.QuestScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
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
