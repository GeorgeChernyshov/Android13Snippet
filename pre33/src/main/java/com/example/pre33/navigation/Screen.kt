package com.example.pre33.navigation

import androidx.annotation.StringRes
import com.example.pre33.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Privacy : Screen("privacy", R.string.label_privacy)
}