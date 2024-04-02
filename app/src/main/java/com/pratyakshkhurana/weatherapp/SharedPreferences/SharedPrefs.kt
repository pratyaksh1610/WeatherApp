package com.pratyakshkhurana.weatherapp.SharedPreferences

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context) {
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPrefs.edit()

    fun saveCountryOrCity(c: String) {
        editor.apply {
            putString(SAVE_COUNTRY_OR_CITY, c)
            apply()
        }
    }

    fun getCountryOrCity(): String {
        return sharedPrefs.getString(SAVE_COUNTRY_OR_CITY, "Delhi").toString()
    }

    companion object {
        const val SHARED_PREFERENCES_TAG = "SHARED_PREFERENCES_TAG"
        const val SAVE_COUNTRY_OR_CITY = "SAVE_COUNTRY_OR_CITY"
    }
}
