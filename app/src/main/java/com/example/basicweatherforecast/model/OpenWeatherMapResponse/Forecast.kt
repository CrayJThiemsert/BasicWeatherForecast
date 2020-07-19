package com.example.basicweatherforecast.model.OpenWeatherMapResponse

import com.google.gson.annotations.SerializedName


data class Forecast(
    @SerializedName("cnt")
    val cnt: Int = 0,
    @SerializedName("list")
    val list: List<Weather> //,
//    @SerializedName("city")
//    val city: City = City()
)