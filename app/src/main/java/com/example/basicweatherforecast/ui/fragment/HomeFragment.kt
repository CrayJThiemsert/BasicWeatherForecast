package com.example.basicweatherforecast.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.basicweatherforecast.R
import com.example.basicweatherforecast.api.RetrofitClient
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.Weather
import com.example.basicweatherforecast.utility.AppUtils
import com.example.basicweatherforecast.utility.TempDegree
import com.mancj.materialsearchbar.MaterialSearchBar
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(AppUtils.mCurrentWeather != null) {
            city_searchbar.text = AppUtils.mCurrentWeather.name
        } else {
            city_searchbar.text = ""
        }

        wholeday_forecast_button.setOnClickListener {
            println("Click button!!!")
            var bundle = bundleOf(
                "cityName" to AppUtils.mCurrentWeather.name,
                "dateTime" to datetime_textview.text.trim().toString())

            findNavController().navigate(R.id.action_homeFragment_to_wholedayForecastFragment, bundle)
        }

        temp_textview.setOnClickListener {
            println("switch degree!!")
            if(AppUtils.mDegree.equals(TempDegree.Celsius)) {
                AppUtils.mDegree = TempDegree.Fahrenheit
            } else {
                AppUtils.mDegree = TempDegree.Celsius
            }

            if(!AppUtils.mCurrentWeather.name.equals("")) {
                getWeatherInformation(AppUtils.mCurrentWeather.name)
            }

        }

        city_searchbar.isEnabled = false
//        val mExecutor : AsyncTaskExecutor = AsyncTaskExecutor.DEFAULT_EXECUTOR
//        mExecutor.execute(LoadCities(context))

        loadSearchBar()

    }

    private fun loadSearchBar() {
        city_searchbar.isEnabled = true
        city_searchbar.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val suggest = ArrayList<String>()

                AppUtils.mCitiesList.forEach() {
                    if(it.name.toLowerCase().contains(city_searchbar.text.toLowerCase())) {
                        suggest.add(it.name)
                    }
                }

                suggest.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it }))
                city_searchbar.lastSuggestions = suggest
            }
        })
        city_searchbar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onButtonClicked(buttonCode: Int) {
                println("onButtonClicked")
            }

            override fun onSearchStateChanged(enabled: Boolean) {
                println("onSearchStateChanged")
            }

            override fun onSearchConfirmed(text: CharSequence?) {
                getWeatherInformation(text.toString())
                val suggest = ArrayList<String>()

                AppUtils.mCitiesList.forEach() {
                    if(it.name.toLowerCase().contains(city_searchbar.text.toLowerCase())) {
                        suggest.add(it.name)
                    }
                }

                suggest.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it }))

                city_searchbar.lastSuggestions = suggest
            }

        } )

        val suggest = mutableListOf<String>()
        AppUtils.mCitiesList.forEach() {
            if(it.name.toLowerCase().contains(city_searchbar.text.toLowerCase())) {
                suggest.add(it.name)
            }
        }
        suggest.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it }))
        city_searchbar.lastSuggestions = suggest

        loading_progressbar.visibility = View.GONE
        weather_layout.visibility = View.VISIBLE

        if(!AppUtils.mCurrentWeather.name.equals("")) {
            getWeatherInformation(AppUtils.mCurrentWeather.name)
        }

    }

    /**
     * Load selected city's weather information
     */
    private fun getWeatherInformation(cityName: String) {
        println("cityName=${cityName}")

        val weatherObservable = getWeatherObservable(cityName, AppUtils.getUnitsType())
        val weatherObserver = getWeatherObserver()

        weatherObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(weatherObserver)

    }

    private fun getWeatherObservable(q: String, units: String): Single<Weather> {
        return RetrofitClient.openWeatherMapMethods().getCurrentWeatherByCityName(AppUtils.mAppId, q, units)
    }

    private fun getWeatherObserver(): DisposableSingleObserver<Weather> {
        return object : DisposableSingleObserver<Weather>() {
            override fun onError(e: Throwable) {
                Log.i("onError", e.toString())
            }

            override fun onSuccess(currentWeather: Weather) {
                AppUtils.mCurrentWeather = currentWeather

                println("currentWeather=" + currentWeather.toString())
                println("mCurrentWeather=" + AppUtils.mCurrentWeather.toString())

                city_textview.setText(currentWeather.name + ", " + AppUtils.getCityInfo(currentWeather.name).country)
                datetime_textview.setText(AppUtils.getLocalFormatDateTimeText(currentWeather.dt))
                temp_textview.setText(AppUtils.getTemperatureText(currentWeather.main.temp))
                humidity_textview.setText(AppUtils.getHumidityText(currentWeather.main.humidity))

            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}