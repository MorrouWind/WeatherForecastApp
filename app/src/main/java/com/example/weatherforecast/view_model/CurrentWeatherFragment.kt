package com.example.weatherforecast.view_model

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentCurrentWeatherBinding
import com.example.weatherforecast.model.WeatherModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

const val DEFAULT_CITY = "Odesa"

class CurrentWeatherFragment : Fragment() {

    private lateinit var binding: FragmentCurrentWeatherBinding
    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[CurrentWeatherViewModel::class.java]

        viewModel.weatherData.observe(viewLifecycleOwner) { weatherData ->
            weatherData?.let { updateUI(it) }
        }

        getWeatherData(DEFAULT_CITY)
        searchCity()
        return binding.root
    }

    private fun searchCity() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                getWeatherData(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun getWeatherData(city: String) {
        viewModel.getWeather(city)
    }

    private fun updateUI(weatherData: WeatherModel) {

        val temperature = weatherData.main.temp
        val tempMax = weatherData.main.tempMax
        val tempMin = weatherData.main.tempMin
        val humidity = weatherData.main.humidity
        val wSpeed = weatherData.wind.speed
        val cityName = weatherData.name
        val conditionTxt = weatherData.weather.firstOrNull()!!.main ?: "unknown".uppercase()
        val pressure = weatherData.main.grndLevel ?: "unknown"
        val sunRise = weatherData.sys.sunrise
        val sunSet = weatherData.sys.sunset

        with(binding) {
            currentTemp.text = temperature?.let { roundOff(it) }
            maxTemp.text = "Max Temp: $tempMax C°"
            minTemp.text = "Min Temp: $tempMin C°"
            location.text = cityName
            humidityPercent.text = "${humidity}%"
            windSpeed.text = "$wSpeed m/s"
            condition.text = conditionTxt
            weatherConditionTxt.text = conditionTxt
            afPressure.text = "$pressure mPa"
            sunrise.text = toTime(sunRise)
            sunset.text = toTime(sunSet)
            dayOfWeek.text = dayOfWeek()
            date.text = currentDate()

            changeImageCondition(conditionTxt)
        }
    }

    private fun changeImageCondition(condition: String) {
        when(condition) {
            "Sunny" , "Clear" -> {
                binding.weatherConditionAnimation.setAnimation(R.raw.sun)
                binding.currentWeatherScreen.setBackgroundResource(R.drawable.sunny_background)
            }
            "Clouds", "Mist", "Fog", "Tornado", "Smoke" -> {
                binding.weatherConditionAnimation.setAnimation(R.raw.cloud)
                binding.currentWeatherScreen.setBackgroundResource(R.drawable.cloud_background)
            }
            "Rain", "Drizzle", "Thunderstorm", "Squall" -> {
                binding.weatherConditionAnimation.setAnimation(R.raw.rain)
                binding.currentWeatherScreen.setBackgroundResource(R.drawable.rain_background)
            }
            "Snow" -> {
                binding.weatherConditionAnimation.setAnimation(R.raw.snow)
                binding.currentWeatherScreen.setBackgroundResource(R.drawable.snow_background)
            }
            else -> {
                binding.weatherConditionAnimation.setAnimation(R.raw.sun)
                binding.currentWeatherScreen.setBackgroundResource(R.drawable.sunny_background)
            }
        }
        binding.weatherConditionAnimation.playAnimation()
    }

    private fun roundOff(value: Double): String {
        return value.roundToInt().toString()
    }

    private fun toTime(value: Int): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeZone = TimeZone.getTimeZone("Europe/Kiev")
        dateFormat.timeZone = timeZone
        return dateFormat.format(Date(value.toLong() * 1000))
    }

    private fun dayOfWeek() : String {
        val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val timeZone = TimeZone.getTimeZone("Europe/Kiev")
        dateFormat.timeZone = timeZone
        return dateFormat.format(Date())
    }

    private fun currentDate() : String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val timeZone = TimeZone.getTimeZone("Europe/Kiev")
        dateFormat.timeZone = timeZone
        return dateFormat.format(Date())
    }
}





