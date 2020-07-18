package com.example.basicweatherforecast.api

import com.example.basicweatherforecast.model.OpenWeatherMapResponse.Forecast
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.Weather
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenWeatherMapMethods {
    @GET("weather?appid=ca319f911d337a0822283361e9e053f5")
    fun getCurrentWeather(@Query("q") q: String, @Query("units") units: String): Single<Weather>

//    @GET("weather")
//    fun getCurrentWeather(
//        @Query("q") q: String,
//        @Query("units") units: String,
//        @Query("appid") appid: String
//    ): Single<Weather>

//    @GET("weather?appid=ca319f911d337a0822283361e9e053f5")
//    fun getCurrentWeatherRx(
//        @Query("q") q: String,
//        @Query("units") units: String
//    ): Observable<Weather>
    @GET("forecast?appid=ca319f911d337a0822283361e9e053f5")
    fun getWholeDayForecast(
        @Query("q") q: String,
        @Query("units") units: String
    ): Single<Forecast>
}