package com.example.basicweatherforecast.model.OpenWeatherMapResponse

import com.google.gson.annotations.SerializedName


data class Weather(
    @SerializedName("dt")
    val dt: Long = System.currentTimeMillis(),
    @SerializedName("main")
    val main: Main = Main(),
    @SerializedName("name")
    val name: String = "",
    @SerializedName("base")
    val base: String = "") {
    override fun toString(): String {
        return "Weather(dt=$dt, main=$main, name='$name', base='$base')"
    }
}