package com.example.basicweatherforecast

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.City
import com.example.basicweatherforecast.utility.AppUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.label305.asynctask.AsyncTaskExecutor
import com.label305.asynctask.SimpleAsyncTask
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream

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

    private var mCitiesList = mutableListOf<City>()

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

        city_searchbar.text = "Bangkok"

        button.setOnClickListener {
            println("Click button!!!")
            findNavController().navigate(R.id.action_homeFragment_to_wholedayForecastFragment, null)
        }

        city_searchbar.isEnabled = false
//        LoadCitiesTask().execute()
        val mExecutor : AsyncTaskExecutor = AsyncTaskExecutor.DEFAULT_EXECUTOR
        mExecutor.execute(LoadCities(context))

    }



    inner class LoadCities(context: Context?) : SimpleAsyncTask<List<City>>() {
        override fun doInBackground(): List<City> {
            var result: List<City> = mutableListOf<City>()
            mCitiesList = mutableListOf<City>()
            try {

                val jsonFileString = AppUtils.getJsonDataFromAsset(context!!, "city_list.json")

//                var builder = StringBuilder()
//                var ips = resources.openRawResource(R.raw.city_list)
//                var gzipInputStream = GZIPInputStream(ips)
//                var reader =  InputStreamReader(gzipInputStream)
//                var bufReader = BufferedReader(reader)


//                result = bufReader.readLines().toMutableList()


                var gson = Gson()
                val listCityType = object : TypeToken<List<City>>() {}.type
                result = gson.fromJson(jsonFileString, listCityType)
                println("cities.size=${result.size}")

//                bufReader.forEachLine {
//                    var city = gson.fromJson(bufReader, City::class.java)
//                    println(city.name)
//                }

//                bufReader.close()
//                reader.close()
//                gzipInputStream.close()
//                ips.close()

//                var readed = bufReader.use { it.readText() }


//                gson.fromJson(readed, TypeToken<List<String>>() {}.type)





            } catch (e: IOException) {
                e.printStackTrace()
            }

            return result
        }

        override fun onSuccess(citiesList: List<City>?) {
            super.onSuccess(citiesList)

            mCitiesList.addAll(citiesList!!)

            city_searchbar.isEnabled = true
            city_searchbar.addTextChangeListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val suggest = mutableListOf<String>()

                    mCitiesList.forEach() {
                        if(it.name.toLowerCase().contains(city_searchbar.text.toLowerCase())) {
                            suggest.add(it.name)
                        }
                    }

                    city_searchbar.lastSuggestions = suggest
                }
            })
            city_searchbar.setOnSearchActionListener(object :MaterialSearchBar.OnSearchActionListener {
                override fun onButtonClicked(buttonCode: Int) {


                }

                override fun onSearchStateChanged(enabled: Boolean) {

                }

                override fun onSearchConfirmed(text: CharSequence?) {
                    getWeatherInformation(text.toString())
                    val suggest = mutableListOf<String>()
                    mCitiesList.forEach() {
                        if(it.name.toLowerCase().contains(city_searchbar.text.toLowerCase())) {
                            suggest.add(it.name)
                        }
                    }

                    city_searchbar.lastSuggestions = suggest
                }

            } )

            val suggest = mutableListOf<String>()
            mCitiesList.forEach() {
                if(it.name.toLowerCase().contains(city_searchbar.text.toLowerCase())) {
                    suggest.add(it.name)
                }
            }
            city_searchbar.lastSuggestions = suggest

            loading_progressbar.visibility = View.GONE
            weather_layout.visibility = View.VISIBLE

        }
    }

    private fun getWeatherInformation(toString: String) {

    }

    //    inner class LoadCitiesTask() : AsyncTask<Void, Void, List<String>>() {
//        override fun doInBackground(vararg params: Void?): List<String> {
//            // ...
//            var result = mutableListOf<String>()
//            mCitiesList = mutableListOf<String>()
//            try {
//                var builder = StringBuilder()
//                var ips = resources.openRawResource(R.raw.city_list)
//                var gzipInputStream = GZIPInputStream(ips)
//                var reader =  InputStreamReader(gzipInputStream)
//                var bufReader = BufferedReader(reader)
//                var readed = bufReader.use { it.readText() }
////                citiesList = bufReader.readLines().toMutableList()
//                var gson = Gson()
////                gson.fromJson(readed, TypeToken<List<String>>() {}.type)
//
//
//
//
//
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//            return result
//        }
//
//        override fun onPreExecute() {
//            super.onPreExecute()
//            // ...
//
//        }
//
//        override fun onPostExecute(citiesList: List<String>?) {
//            super.onPostExecute(citiesList)
//            // ...
//            city_searchbar.isEnabled = true
//            city_searchbar.addTextChangeListener(object : TextWatcher {
//                override fun afterTextChanged(s: Editable?) {
//                }
//
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    val suggest = mutableListOf<String>()
//
//                    mCitiesList.forEach() {
//                        if(it.toLowerCase().contains(city_searchbar.text.toLowerCase())) {
//                            suggest.add(it)
//                        }
//                    }
//                }
//            })
//        }
//    }

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