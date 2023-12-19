package com.example.pre33

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pre33.navigation.Screen
import com.example.pre33.screen.privacy.PrivacyScreen
import com.example.pre33.theme.Android13SnippetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { App() }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()

    Android13SnippetTheme {
        NavHost(
            navController = navController,
            startDestination = Screen.Privacy.route
        ) {
            composable(Screen.Privacy.route) {
                PrivacyScreen()
            }
        }
    }
}