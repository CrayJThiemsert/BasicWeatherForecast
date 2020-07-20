package com.example.basicweatherforecast.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicweatherforecast.R
import com.example.basicweatherforecast.api.RetrofitClient
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.City
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.Hourly
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.OneCall
import com.example.basicweatherforecast.ui.adapter.OneCallAdapter
import com.example.basicweatherforecast.utility.AppUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.loading_progressbar

import kotlinx.android.synthetic.main.fragment_wholeday_forecast.*
import kotlinx.android.synthetic.main.fragment_wholeday_forecast.city_textview
import kotlinx.android.synthetic.main.fragment_wholeday_forecast.datetime_textview

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "cityName"
private const val ARG_PARAM2 = "dateTime"

/**
 * A simple [Fragment] subclass.
 * Use the [WholedayForecastFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WholedayForecastFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mOneCallAdapter: OneCallAdapter
    private var mWeatherList = ArrayList<Hourly>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wholeday_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("param1=${param1}")

        val city = AppUtils.getCityInfo(param1!!)

        println("onViewCreated: city=${city}")

        city_textview.setText("${param1}, ${city.country}")
        datetime_textview.setText(param2)

        back_button.setOnClickListener {
            println("Click back button!!!")
            this.findNavController().popBackStack()
//            findNavController().navigate(R.id.action_wholedayForecastFragment_to_homeFragment, null)
        }

        getWholeDayForecast(city, requireContext())
    }

    private fun getWholeDayForecast(
        city: City,
        context: Context
    ) {
        println("getWholeDayForecast : cityName=${city.name}")

        mOneCallAdapter =
            OneCallAdapter(
                context,
                mWeatherList
            )
        current_weather_recyclerview.setHasFixedSize(true)
        current_weather_recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        current_weather_recyclerview.adapter = mOneCallAdapter


        val forecastObservable = getForecastObservable(city, AppUtils.getUnitsType())
        val forecastObserver = getForecastObserver()

        forecastObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(forecastObserver)

    }

    private fun getForecastObservable(city: City, units: String): Single<OneCall> {
        return RetrofitClient.openWeatherMapMethods().getOneWholeDayForecastByLatLon(AppUtils.mAppId, city.coord.lat.toString(), city.coord.lon.toString(), units)
    }

    private fun getForecastObserver(): DisposableSingleObserver<OneCall> {
        return object : DisposableSingleObserver<OneCall>() {
            override fun onError(e: Throwable) {
                Log.i("onError", e.toString())
                loading_progressbar.visibility = View.GONE
            }

            override fun onSuccess(oneCall: OneCall) {
//                textView2.setText(oneCall.timezone + "-" + AppUtils.getLocalFormatDateTime(oneCall.current.dt) + " | "+ oneCall.current.temp + " | "+ oneCall.current.humidity)
                loading_progressbar.visibility = View.GONE
                wholeday_forecast_layout.visibility = View.VISIBLE

                mWeatherList.addAll(getOnlyWholeDay(oneCall))
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WholedayForecastFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WholedayForecastFragment()
                .apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}