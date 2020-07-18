package com.example.basicweatherforecast

import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.basicweatherforecast.api.RetrofitClient
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.Forecast
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.Weather
import com.example.basicweatherforecast.utility.SetUpLayoutManager
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_current_weather.*

class WholeDayForecastActivity: AppCompatActivity() {

    private lateinit var mForecastAdapter: ForecastAdapter
    private val mWeatherList = ArrayList<Weather>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_weather)

        SetUpLayoutManager.verticalLinearLayout(applicationContext, current_weather_recyclerview)

        mForecastAdapter = ForecastAdapter(applicationContext, mWeatherList)
        current_weather_recyclerview.adapter = mForecastAdapter

        val forecastObservable = getForecastObservable("London", "metric")
        val forecastObserver = getForecastObserver()

        forecastObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap { forecast ->
//                    forecast.list.toObservable()
//                }
//                .filter {
//                    it.name.toLowerCase().startsWith("s")
//                }
//                .toList()
                .subscribe(forecastObserver)
    }

    private fun getForecastObservable(q: String, units: String): Single<Forecast> {
        return RetrofitClient.openWeatherMapMethods().getWholeDayForecast(q, units)
    }

    private fun getForecastObserver(): DisposableSingleObserver<Forecast> {
        return object : DisposableSingleObserver<Forecast>() {
            override fun onError(e: Throwable) {
                Log.i("onError", e.toString())
            }

            override fun onSuccess(forecast: Forecast) {
                mWeatherList.clear()
                mWeatherList.addAll(forecast.list)
//                mForecastList.add(forecast)
                mForecastAdapter.notifyDataSetChanged()
            }
        }
    }
}