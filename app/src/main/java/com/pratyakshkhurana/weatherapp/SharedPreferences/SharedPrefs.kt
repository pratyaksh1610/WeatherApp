package com.pratyakshkhurana.weatherapp.SharedPreferences

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context) {
    private val latitudeTag = "LATITUDE_TAG"
    private val longitudeTag = "LONGITUDE_TAG"
    private val locationPermissionGranted = "LOCATION_PERMISSION_GRANTED_TAG"
    private val sharedPreferencesTag = "SHARED_PREFERENCES_TAG"

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(sharedPreferencesTag, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPrefs.edit()

    fun saveLatitude(lat: Float) {
        editor.apply {
            putFloat(latitudeTag, lat)
            commit()
        }
    }

    fun saveLongitude(long: Float) {
        editor.apply {
            putFloat(longitudeTag, long)
            commit()
        }
    }

    fun getLatitude(): Float {
        return sharedPrefs.getFloat(latitudeTag, 0f)
    }

    fun getLongitude(): Float {
        return sharedPrefs.getFloat(longitudeTag, 0f)
    }

    fun isLocationPermissionGranted(): Boolean {
        return sharedPrefs.getBoolean(locationPermissionGranted, false)
    }

    fun saveLocationPermissionGrantedResult(code: Boolean) {
        editor.apply {
            putBoolean(locationPermissionGranted, code)
            commit()
        }
    }
}
