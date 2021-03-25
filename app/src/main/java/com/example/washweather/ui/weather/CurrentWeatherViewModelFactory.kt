package com.example.washweather.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.washweather.data.repository.WeatherRepository

@Suppress("UNCHECKED_CAST")
class CurrentWeatherViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(repository) as T
    }
}
