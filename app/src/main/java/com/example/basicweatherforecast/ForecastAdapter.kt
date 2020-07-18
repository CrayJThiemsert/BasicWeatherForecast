package com.example.basicweatherforecast

import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import com.example.basicweatherforecast.model.OpenWeatherMapResponse.Forecast
import com.example.basicweatherforecast.model.OpenWeatherMapResponse.Weather
import kotlinx.android.synthetic.main.item_github_user.view.*

class ForecastAdapter(private val mContext: Context, private val mWeatherList: List<Weather>) : RecyclerView.Adapter<ForecastAdapter.CurrentWeatherViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CurrentWeatherViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_current_weather, viewGroup, false)

        return CurrentWeatherViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CurrentWeatherViewHolder, position: Int) {
        val currentWeather = getItem(position)

        holder.nameTextView.text = if (currentWeather.main.temp != null)  currentWeather.main.temp.toString() else "0.0"
        holder.typeTextView.text = if (currentWeather.main.humidity != null)  currentWeather.main.humidity.toString() else "0.0"
    }

    override fun getItemCount(): Int {
        return mWeatherList.size
    }

    fun getItem(position: Int): Weather {
        return mWeatherList[position]
    }

    class CurrentWeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.nameTextView
        val typeTextView: TextView = itemView.typeTextView
    }
}