package com.example.basicweatherforecast.model.OpenWeatherMapResponse

import com.google.gson.annotations.SerializedName

data class City(
                @SerializedName("id")
                val id: Long = 0,
                @SerializedName("name")
                val name: String = "",
                @SerializedName("country")
                val country: String = "",
                @SerializedName("coord")
                val coord: Coord
                )

{
    override fun toString(): String {
        return "City(id=$id, name='$name', country='$country', coord=$coord)"
    }
}