package com.pratyakshkhurana.weatherapp.Api

import com.pratyakshkhurana.weatherapp.DataClass.CurrentWeather
import com.pratyakshkhurana.weatherapp.DataClass.EveryThreeHourWeatherForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    // metric units
    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") q: String,
        @Query("appId") appID: String,
        @Query("units") units: String = "metric",
    ): Response<CurrentWeather>

    @GET("data/2.5/forecast")
    suspend fun getCurrentWeatherEveryThreeHour(
        @Query("q") q: String,
        @Query("appId") appID: String,
        @Query("units") units: String = "metric",
    ): Response<EveryThreeHourWeatherForecast>
}
