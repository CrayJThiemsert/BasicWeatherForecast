package com.example.basicweatherforecast.model.OpenWeatherMapResponse

import com.google.gson.annotations.SerializedName

data class City(@SerializedName("name")
                    val name: String = "",
                @SerializedName("country")
                    val country: String = ""
                )