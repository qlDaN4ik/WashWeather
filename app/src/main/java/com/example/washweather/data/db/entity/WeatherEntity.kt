package com.example.washweather.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "weather")
data class WeatherEntity(
        @PrimaryKey(autoGenerate = false)
        var id: Int = 0,
        var humidity: Int = 0,
        var pressure: Int = 0,
        var temp: Int = 0,
        var tempMax: Int = 0,
        var tempMin: Int = 0,
        @Embedded(prefix = "wind_")
        var wind: Wind = Wind(),
        var description: String = "",
        @Embedded(prefix = "advices_")
        var advices: Advices,
        var icon: Int = 0,
        @Embedded(prefix = "location_")
        var location: Location = Location(),
        var lastUpdate: Long = 0L
)
