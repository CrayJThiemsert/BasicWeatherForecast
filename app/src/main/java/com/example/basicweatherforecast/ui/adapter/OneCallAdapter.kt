package com.example.basicweatherforecast.ui.adapter

import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.basicweatherforecast.R
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.Hourly
//import com.example.basicweatherforecast.model.OpenWeatherMapResponse.Forecast
import com.example.basicweatherforecast.utility.AppUtils
import kotlinx.android.synthetic.main.item_forecast.view.*
import kotlinx.android.synthetic.main.item_forecast.view.temp_textview
import kotlinx.android.synthetic.main.item_forecast.view.humidity_textview

class OneCallAdapter(private val mContext: Context, private val mWeatherList: List<Hourly>) : RecyclerView.Adapter<OneCallAdapter.CurrentWeatherViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CurrentWeatherViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_forecast, viewGroup, false)

        return CurrentWeatherViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: CurrentWeatherViewHolder, position: Int) {
        val currentWeather = getItem(position)
//        val dt = AppUtils.getLocalFormatDateTime(currentWeather.dt)
        val dt = AppUtils.getLocalFormatTime(currentWeather.dt)

        holder.datetime_textview.text = dt

        val temp = AppUtils.getTemperatureText(currentWeather.temp)

        holder.temp_textview.text = temp
        val humidity = AppUtils.getHumidityText(currentWeather.humidity)
        holder.humidity_textview.text = humidity
    }

    override fun getItemCount(): Int {
        return mWeatherList.size
    }

    fun getItem(position: Int): Hourly {
        return mWeatherList[position]
    }

    class CurrentWeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val temp_textview: TextView = itemView.temp_textview
        val humidity_textview: TextView = itemView.humidity_textview
        val datetime_textview: TextView = itemView.datetime_textview
    }
}