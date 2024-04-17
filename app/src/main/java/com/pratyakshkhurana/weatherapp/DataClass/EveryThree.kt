package com.pratyakshkhurana.weatherapp.DataClass

data class EveryThree(
    val clouds: CloudsX,
    val dt: Int,
    val dt_txt: String,
    val main: MainX,
    val pop: Double,
    val rain: Rain,
    val snow: SnowX,
    val sys: SysX,
    val visibility: Int,
    val weather: List<WeatherX>,
    val wind: WindX,
)
