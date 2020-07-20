package com.example.basicweatherforecast.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val OPEN_WEATHER_MAP_BASE_URL = "http://api.openweathermap.org/data/2.5/"

    fun openWeatherMapMethods(): OpenWeatherMapMethods {
        val retrofit = Retrofit.Builder()
            .baseUrl(OPEN_WEATHER_MAP_BASE_URL)
            .client(OkHttpClient().newBuilder().build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(OpenWeatherMapMethods::class.java)
    }
}