package com.example.basicweatherforecast.model.OpenWeatherMapResponse

import com.google.gson.annotations.SerializedName

data class Coord(@SerializedName("lon")
                    val lon: Double = 0.0,
                 @SerializedName("lat")
                    val lat: Double = 0.0
                )
{
    override fun toString(): String {
        return "Coord(lon=$lon, lat=$lat)"
    }
}