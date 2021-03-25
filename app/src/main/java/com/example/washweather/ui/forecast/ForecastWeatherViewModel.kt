package com.example.washweather.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.washweather.data.db.entity.NoteEntity
import com.example.washweather.data.db.entity.WeatherEntity
import com.example.washweather.data.repository.WeatherRepository

class ForecastWeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    fun getForecastWeather(): LiveData<List<WeatherEntity>> {
        return repository.getForecastWeather()
    }
    fun getAllNote(): LiveData<List<NoteEntity>> {
        return repository.getAllNote()
    }
    fun insertNote(item : NoteEntity) {
        repository.insertNote(item)
    }
    fun updateNote(item : NoteEntity) {
        repository.updateNote(item)
    }
    fun deleteNote(item : NoteEntity) {
        repository.deleteNote(item)
    }
}