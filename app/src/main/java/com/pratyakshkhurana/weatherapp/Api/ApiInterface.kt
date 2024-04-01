package com.pratyakshkhurana.weatherapp.Api

import com.pratyakshkhurana.weatherapp.DataClass.CurrentWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("/weather")
    fun getCurrentWeather(
        @Query("q") q: String,
        @Query("appId") appID: String,
    ): Call<CurrentWeather>
}
