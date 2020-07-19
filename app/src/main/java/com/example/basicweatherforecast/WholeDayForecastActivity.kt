package com.example.basicweatherforecast

import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicweatherforecast.api.RetrofitClient
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.Hourly
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.OneCall
import com.example.basicweatherforecast.utility.AppUtils
import com.example.basicweatherforecast.utility.SetUpLayoutManager
import com.example.basicweatherforecast.utility.TempDegree
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_current_weather.*

class WholeDayForecastActivity: AppCompatActivity() {

    private lateinit var mOneCallAdapter: OneCallAdapter
    private var mWeatherList = ArrayList<Hourly>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wholeday_forecast)

        SetUpLayoutManager.verticalLinearLayout(applicationContext, current_weather_recyclerview)

        mOneCallAdapter = OneCallAdapter(this, mWeatherList)
        current_weather_recyclerview.setHasFixedSize(true)
        current_weather_recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        current_weather_recyclerview.adapter = mOneCallAdapter


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

    private fun getForecastObservable(q: String, units: String): Single<OneCall> {
//        return RetrofitClient.openWeatherMapMethods().getWholeDayForecast(q, units)
        return RetrofitClient.openWeatherMapMethods().getOneWholeDayForecast(units)
    }

    private fun getForecastObserver(): DisposableSingleObserver<OneCall> {
        return object : DisposableSingleObserver<OneCall>() {
            override fun onError(e: Throwable) {
                Log.i("onError", e.toString())
            }

            override fun onSuccess(oneCall: OneCall) {
                textView2.setText(oneCall.timezone + "-" + AppUtils.getLocalFormatDateTime(oneCall.current.dt) + " | "+ oneCall.current.temp + " | "+ oneCall.current.humidity)

                mWeatherList.addAll(getOnlyWholeDay(oneCall))
//                mWeatherList.clear()
//                mWeatherList.addAll(getOnlyWholeDay(oneCall.hourly))
//                mForecastList.add(forecast)
                mOneCallAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun getOnlyWholeDay(oneCall: OneCall):  ArrayList<Hourly> {
        var weatherList = ArrayList<Hourly>()
        weatherList.clear()
        oneCall.hourly.forEach { hourly ->
            if(AppUtils.getLocalDate(oneCall.current.dt).toString().equals(AppUtils.getLocalDate(hourly.dt).toString())) {
                weatherList.add(hourly)
            }
        }
        return weatherList
    }
}