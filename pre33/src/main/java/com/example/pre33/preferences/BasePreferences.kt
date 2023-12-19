package com.example.pre33.preferences

import android.content.SharedPreferences

abstract class BasePreferences(
    private val prefs: SharedPreferences
) {
    protected fun booleanField(
        fieldName: String,
        defaultValue: Boolean = false) = object : BaseField<Boolean>(prefs, fieldName) {

        override fun put(editor: SharedPreferences.Editor, value: Boolean) {
            editor.putBoolean(fieldName, value)
        }

        override fun get() = prefs.getBoolean(fieldName, defaultValue)
    }
}