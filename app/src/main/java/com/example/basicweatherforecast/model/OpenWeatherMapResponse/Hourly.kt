package com.example.basicweatherforecast.model.OpenWeatherMapResponse

import com.google.gson.annotations.SerializedName

data class Hourly(
    @SerializedName("dt")
    val dt: Long = System.currentTimeMillis(),
    @SerializedName("temp")
    val temp: Double = 0.0,
    @SerializedName("humidity")
    val humidity: Int = 0
    )