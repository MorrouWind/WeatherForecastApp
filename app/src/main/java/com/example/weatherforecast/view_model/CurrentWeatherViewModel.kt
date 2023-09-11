package com.example.weatherforecast.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.WeatherModel
import com.example.weatherforecast.service.RetrofitAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrentWeatherViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherModel?>()
    val weatherData: LiveData<WeatherModel?> = _weatherData

    fun getWeather(city: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = RetrofitAPI.api.getCurrentWeatherByCity(city)
        response.enqueue(object : Callback<WeatherModel> {
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _weatherData.value = responseBody
                }
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                Log.d("Fail", "Fail")
            }
        })
    }
}
