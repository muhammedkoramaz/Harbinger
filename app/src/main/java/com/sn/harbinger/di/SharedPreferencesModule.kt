package com.sn.harbinger.di

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferencesModule @Inject constructor(@ApplicationContext context: Context) {
    private val prefsName = "harbinger_preferences"
    private val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    fun getToken(): String {
        return prefs.getString("token", "")!!
    }

    fun setToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    fun removeToken() {
        prefs.edit().remove("token").apply()
    }

}