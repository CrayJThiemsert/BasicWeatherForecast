package com.example.basicweatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.City
import com.example.basicweatherforecast.ui.fragment.HomeFragment
import com.example.basicweatherforecast.utility.AppUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.label305.asynctask.AsyncTaskExecutor
import com.label305.asynctask.SimpleAsyncTask
import java.io.IOException

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load cities data
        loadData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        for(fragment in supportFragmentManager.fragments) {
            if(fragment is HomeFragment) {
                finishAffinity()
                return
            }
        }

    }

    private fun loadData() {
        val mExecutor : AsyncTaskExecutor = AsyncTaskExecutor.DEFAULT_EXECUTOR
        mExecutor.execute(LoadCities())
    }



    /**
     * Load cities list
     */
    inner class LoadCities() : SimpleAsyncTask<List<City>>() {
        override fun doInBackground(): List<City> {
            var result: List<City> = ArrayList<City>()
            AppUtils.mCitiesList = ArrayList<City>()
            try {

                val jsonFileString = AppUtils.getJsonDataFromAsset(this@MainActivity!!, "city_list.json")

                var gson = Gson()
                val listCityType = object : TypeToken<List<City>>() {}.type
                result = gson.fromJson(jsonFileString, listCityType)
                println("cities.size=${result.size}")

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return result
        }

        override fun onSuccess(citiesList: List<City>?) {
            super.onSuccess(citiesList)

            AppUtils.mCitiesList.addAll(citiesList!!)
        }
    }

}