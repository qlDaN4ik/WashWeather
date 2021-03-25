package com.example.washweather.data.repository

import androidx.lifecycle.LiveData
import com.example.washweather.data.db.entity.NoteEntity
import com.example.washweather.data.db.entity.WeatherEntity

interface WeatherRepository {
    fun getForecastWeather(): LiveData<List<WeatherEntity>>
    fun getCurrentWeather(): LiveData<WeatherEntity>
    fun insertNote(item : NoteEntity)
    fun getAllNote(): LiveData<List<NoteEntity>>
    fun updateNote(item : NoteEntity)
    fun deleteNote(item : NoteEntity)
}
