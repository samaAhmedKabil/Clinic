package com.example.clinic.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager (context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("clinic_prefs", Context.MODE_PRIVATE)

    fun setRememberMe(checked: Boolean) {
        prefs.edit().putBoolean("remember_me", checked).apply()
    }

    fun isRememberMe(): Boolean {
        return prefs.getBoolean("remember_me", false)
    }

    fun clearPrefs() {
        prefs.edit().clear().apply()
    }

    fun saveUserName(name: String) {
        prefs.edit().putString("user_name", name).apply()
    }

    fun getUserName(): String {
        return prefs.getString("user_name", "") ?: ""
    }
}