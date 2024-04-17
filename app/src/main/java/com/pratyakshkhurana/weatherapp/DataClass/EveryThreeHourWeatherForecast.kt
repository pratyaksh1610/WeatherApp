package com.pratyakshkhurana.weatherapp.DataClass

data class EveryThreeHourWeatherForecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<EveryThree>,
    val message: Int,
)
