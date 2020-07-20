package com.example.basicweatherforecast.utility

import android.content.Context
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.City
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.Coord
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.Weather
import java.io.IOException
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

enum class TempDegree {
    Celsius, Fahrenheit
}

class AppUtils {


    companion object {
        var mDegree = TempDegree.Celsius
        val mAppId = "ca319f911d337a0822283361e9e053f5"

        var mCitiesList = mutableListOf<City>()
        var mCurrentWeather: Weather = Weather()

        /**
         * Get City information (Coord)
         */
        fun getCityInfo(cityName: String) : City {
            // Clean non English characters
            val clean = Normalizer.normalize(cityName, Normalizer.Form.NFD)
                .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")

            var result = City(coord = Coord())
            mCitiesList.forEach() {
                if(it.name.equals(clean)) {
                    result = it
                }
            }
            return result
        }

        fun getLocalFormatDateTime(value: Long): String {
            val dt: Long = if (value != null) value else 0

            val result = DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochSecond(dt))

//        val localDateTime = Instant.ofEpochMilli(dt).atZone(ZoneId.systemDefault()).toLocalDateTime()
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            formatter.timeZone = TimeZone.getDefault()
            val localDateTime = Instant.ofEpochSecond(dt)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
//            val temp = formatter.format(localDateTime)


            if (dt > 0) {
                return localDateTime.toString()
            } else {
                return "..."
            }
        }

        fun getLocalFormatDateTimeText(value: Long): String {
            val dt: Long = if (value != null) value else 0

//            val result = DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochSecond(dt))

//        val localDateTime = Instant.ofEpochMilli(dt).atZone(ZoneId.systemDefault()).toLocalDateTime()
            val formatter = DateTimeFormatter.ofPattern("EEE, MMMM dd, yyyy HH:mm", Locale.getDefault())
            val localDateTime = Instant.ofEpochSecond(dt)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(formatter)
            var result = "..."


            if (dt > 0) {
                result = localDateTime.toString()
//                return //localDateTime.toString()
            }
            return result
        }

        fun getLocalFormatTime(value: Long): String {
            val dt: Long = if (value != null) value else 0

            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            formatter.timeZone = TimeZone.getDefault()
            val localTime = Instant.ofEpochSecond(dt)
                .atZone(ZoneId.systemDefault())
                .toLocalTime()

            if (dt > 0) {
                return localTime.toString()
            } else {
                return "..."
            }
        }

        public fun getLocalDate(value: Long): String {
            val dt: Long = if (value != null) value else 0

            val localDate = Instant.ofEpochSecond(dt)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()


            if (dt > 0) {
                return localDate.toString()
            } else {
                return "..."
            }
        }

        public fun getUTCFormatDateTime(value: Long): String {
            val dt: Long = if (value != null) value else 0

            val result = DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochSecond(dt))

            if (dt > 0) {
                return result
            } else {
                return "..."
            }
        }

        /**
         * Create temperature value for display
         */
        fun getTemperatureText(temp: Double): String {
            var result = ""
            if (temp == null)  return "..."

            if(mDegree.equals(TempDegree.Celsius)) {
                // Celsius
                result = "${temp} \u2103"
            } else {
                // Fahrenheit
                result = "${temp} \u2109"
            }
            return result
        }

        /**
         * Create temperature value for display
         */
        fun getHumidityText(temp: Int): String {
            var result = ""
            if (temp == null)  return "..."
            result = "${temp}%"
            return result
        }

        fun getJsonDataFromAsset(context: Context, fileName: String): String? {
            val jsonString: String
            try {
                jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }

        /**
         * Get Units format type
         * For temperature in Fahrenheit use units=imperial
         * For temperature in Celsius use units=metric
         */
        fun getUnitsType(): String {
            var result = "metric"

            if(mDegree.equals(TempDegree.Celsius)) {
                // Celsius
                result = "metric"
            } else {
                // Fahrenheit
                result = "imperial"
            }
            return result
        }
    }
}