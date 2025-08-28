package com.example.post33

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.post33.navigation.Screen
import com.example.post33.screen.performance.PerformanceScreen
import com.example.post33.screen.performance.PrefetchJobService
import com.example.post33.screen.privacy.PrivacyScreen
import com.example.post33.screen.speech.SpeechScreen
import com.example.post33.screen.wifipermission.WiFiPermissionScreen
import com.example.post33.theme.Android13SnippetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scheduleJob()
        setContent { App() }
    }

    private fun scheduleJob() {
        val componentName = ComponentName(this, PrefetchJobService::class.java)
        val jobInfo = JobInfo.Builder(123, componentName)
            .setPrefetch(true)
            .build()

        getSystemService(JobScheduler::class.java)
            .schedule(jobInfo)
    }
}

@Composable
fun App() {
    val navController = rememberNavController()

    Android13SnippetTheme {
        NavHost(
            navController = navController,
            startDestination = Screen.Performance.route
        ) {
            composable(Screen.Performance.route) {
                PerformanceScreen {
                    navController.navigate(Screen.Privacy.route)
                }
            }

            composable(Screen.Privacy.route) {
                PrivacyScreen {
                    navController.navigate(Screen.WifiPermission.route)
                }
            }

            composable(Screen.Speech.route) {
                SpeechScreen()
            }

            composable(Screen.WifiPermission.route) {
                WiFiPermissionScreen()
            }
        }
    }
}