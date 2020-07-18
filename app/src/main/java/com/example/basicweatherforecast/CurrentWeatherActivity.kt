package com.example.basicweatherforecast

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.basicweatherforecast.api.RetrofitClient
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.Weather
import com.example.basicweatherforecast.utility.SetUpLayoutManager
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_current_weather.*

class CurrentWeatherActivity : AppCompatActivity() {

    private val mCurrentWeatherList = ArrayList<Weather>()
    private lateinit var mCurrentWeatherAdapter: CurrentWeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_weather)

        SetUpLayoutManager.verticalLinearLayout(applicationContext, current_weather_recyclerview)

        mCurrentWeatherAdapter = CurrentWeatherAdapter(applicationContext, mCurrentWeatherList)
        current_weather_recyclerview.adapter = mCurrentWeatherAdapter

//        val weatherObservable = getWeatherObservable("Bangkok", "metric", "ca319f911d337a0822283361e9e053f5")
        val weatherObservable = getWeatherObservable("Bangkok", "metric")
        val weatherObserver = getWeatherObserver()

        weatherObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherObserver)


    }

    private fun getWeatherObservable(q: String, units: String): Single<Weather> {
        return RetrofitClient.openWeatherMapMethods().getCurrentWeather(q, units)
    }

    private fun getWeatherObserver(): DisposableSingleObserver<Weather> {
        return object : DisposableSingleObserver<Weather>() {
            override fun onError(e: Throwable) {
                Log.i("onError", e.toString())
            }

            override fun onSuccess(currentWeatherList: Weather) {
                mCurrentWeatherList.clear()

//                mCurrentWeatherList.addAll(currentWeatherList)
                mCurrentWeatherList.add(currentWeatherList)
                mCurrentWeatherAdapter.notifyDataSetChanged()
            }
        }
    }
}