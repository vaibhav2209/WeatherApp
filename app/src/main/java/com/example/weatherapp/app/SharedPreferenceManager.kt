package com.example.weatherapp.app

import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject

class SharedPreferenceManager @Inject constructor(
    private val context: Context
) {

    companion object {
        const val SESSION_PREFERENCE = "session_preference"

        const val IS_LOGGED_IN = "Is_logged_in"
    }

    private val sharedPref = context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        sharedPref.edit {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String) {
        sharedPref.getString(key, "")
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPref.edit {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBoolean(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }
}