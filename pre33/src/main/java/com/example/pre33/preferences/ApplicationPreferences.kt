package com.example.pre33.preferences

import android.content.SharedPreferences

class ApplicationPreferences(
    prefs: SharedPreferences
) : BasePreferences(prefs) {

    val notifications = booleanField("notifications", false)
}