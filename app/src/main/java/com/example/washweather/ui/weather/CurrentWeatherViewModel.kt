package com.example.washweather.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.washweather.data.db.entity.WeatherEntity
import com.example.washweather.data.repository.WeatherRepository

class CurrentWeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private var showProgress: MutableLiveData<Boolean> = MutableLiveData()

    fun getWeather(): LiveData<WeatherEntity> {
        showProgress.postValue(true)
        return Transformations.map(repository.getCurrentWeather()) {
            showProgress.postValue(false)
            return@map it
        }
    }

    fun getProgressState(): MutableLiveData<Boolean> {
        return showProgress
    }
}