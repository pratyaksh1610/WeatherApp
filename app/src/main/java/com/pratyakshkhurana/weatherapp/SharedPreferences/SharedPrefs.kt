package com.pratyakshkhurana.weatherapp.SharedPreferences

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context) {
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPrefs.edit()

    fun saveLatitude(lat: String) {
        editor.apply {
            putString(LATITUDE_TAG, lat)
            commit()
        }
    }

    fun saveLongitude(long: String) {
        editor.apply {
            putString(LONGITUDE_TAG, long)
            commit()
        }
    }

    fun getLatitude(): String? {
        return sharedPrefs.getString(LATITUDE_TAG, "0")
    }

    fun getLongitude(): String? {
        return sharedPrefs.getString(LONGITUDE_TAG, "0")
    }

    companion object {
        const val SHARED_PREFERENCES_TAG = "SHARED_PREFERENCES_TAG"
        const val LATITUDE_TAG = "LATITUDE_TAG"
        const val LONGITUDE_TAG = "LONGITUDE_TAG"
    }
}
