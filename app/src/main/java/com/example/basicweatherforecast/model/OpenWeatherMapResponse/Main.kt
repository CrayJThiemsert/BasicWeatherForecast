package com.example.basicweatherforecast.model.OpenWeatherMapResponse

import com.google.gson.annotations.SerializedName

data class Main(@SerializedName("temp")
                    val temp: Double = 0.0,
                @SerializedName("humidity")
                    val humidity: Int = 0
                ) {
    override fun toString(): String {
        return "Main(temp=$temp, humidity=$humidity)"
    }
}