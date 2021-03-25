package com.example.washweather.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.washweather.data.db.WeatherDAO
import com.example.washweather.data.db.entity.NoteEntity
import com.example.washweather.data.db.entity.WeatherEntity
import com.example.washweather.data.net.WeatherNetworkDataSource
import com.example.washweather.data.net.owm.OWNetworkDataSourceImpl


class WeatherRepositoryImpl(
        private val weatherDAO: WeatherDAO,
        private val owNetworkDataSourceImpl: OWNetworkDataSourceImpl
) : WeatherRepository {

    private val language = "ru"
    private val city = "Krasnoyarsk"
    private val units= "metric"
    private lateinit var weatherNetworkDataSource: WeatherNetworkDataSource

    override fun getForecastWeather(): LiveData<List<WeatherEntity>> {
        weatherNetworkDataSource = owNetworkDataSourceImpl

        GlobalScope.launch {
            val fetchForecastWeather = withContext(Dispatchers.IO) {
                weatherNetworkDataSource.getForecastWeather(city, units, language)
            }
            weatherDAO.insertAll(fetchForecastWeather)
        }

        return weatherDAO.getWeathers()
    }

    override fun getCurrentWeather(): LiveData<WeatherEntity> {
        return weatherDAO.getCurrentWeather()
    }

    override fun insertNote(item : NoteEntity) {
        weatherDAO.insertNote(item)
    }

    override fun getAllNote(): LiveData<List<NoteEntity>> {
        return weatherDAO.getAllNote()
    }

    override fun updateNote(item : NoteEntity) {
        weatherDAO.updateNote(item)
    }

    override fun deleteNote(item : NoteEntity) {
        weatherDAO.deleteNote(item)
    }
}