package com.example.washweather.data.net

import com.example.washweather.data.db.entity.WeatherEntity

interface WeatherNetworkDataSource {
    suspend fun getForecastWeather(
            location: String,
            units: String,
            language: String
    ): List<WeatherEntity>{
        return emptyList()
    }
}