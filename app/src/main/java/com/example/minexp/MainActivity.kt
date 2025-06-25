package com.example.minexp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.minexp.ui.screen.MainScreen
import com.example.minexp.ui.theme.MindXPTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MindXPTheme {
                MainScreen()
            }
        }
    }
}

