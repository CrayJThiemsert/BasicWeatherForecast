package com.example.basicweatherforecast.model.OpenWeatherMapResponse

import com.google.gson.annotations.SerializedName


data class OneCall(
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("lon")
    val lon: Double = 0.0,
    @SerializedName("hourly")
    val hourly: List<Hourly>,
    @SerializedName("current")
    val current: Hourly,
    @SerializedName("timezone")
    val timezone: String = "")