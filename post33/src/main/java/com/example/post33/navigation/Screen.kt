package com.example.post33.navigation

import androidx.annotation.StringRes
import com.example.post33.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Performance : Screen("performance", R.string.label_performance)
    object Privacy : Screen("privacy", R.string.label_privacy)
    object Speech : Screen("speech", R.string.label_speech)
    object WifiPermission : Screen("wifiPermission", R.string.label_wifi_permission)
}