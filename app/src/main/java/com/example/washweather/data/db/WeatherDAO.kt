package com.example.washweather.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.washweather.data.db.entity.CURRENT_WEATHER_ID
import com.example.washweather.data.db.entity.NoteEntity
import com.example.washweather.data.db.entity.WeatherEntity

@Dao
interface WeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherEntity: WeatherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(weatherEntity: List<WeatherEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(weatherEntity: WeatherEntity)

    @Query("select * from weather where id = $CURRENT_WEATHER_ID")
    fun getCurrentWeather(): LiveData<WeatherEntity>

    @Query("select * from weather where id = :id")
    fun getWeatherById(id: Int): LiveData<WeatherEntity>

    @Query("SELECT * FROM weather ORDER BY id")
    fun getWeathers(): LiveData<List<WeatherEntity>>

    @Query("SELECT lastUpdate FROM weather where id = $CURRENT_WEATHER_ID")
    fun getLastUpdate(): LiveData<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(noteEntity: NoteEntity)

    @Query("select * from note")
    fun getAllNote(): LiveData<List<NoteEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateNote(noteEntity: NoteEntity)

    @Delete
    fun deleteNote(noteEntity: NoteEntity)
}