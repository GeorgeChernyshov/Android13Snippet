package com.example.pre33.preferences

import android.content.SharedPreferences

abstract class BaseField<T>(
    protected val prefs: SharedPreferences,
    private val fieldName: String
) {
    fun putApply(value: T) {
        val editor = prefs.edit()
        put(editor, value)
        editor.apply()
    }

    fun clearApply() {
        prefs.edit().remove(fieldName).apply()
    }

    protected abstract fun put(editor: SharedPreferences.Editor, value: T)

    abstract fun get(): T
}