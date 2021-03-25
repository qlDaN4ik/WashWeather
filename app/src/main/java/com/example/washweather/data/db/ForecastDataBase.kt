package com.example.washweather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.washweather.data.db.entity.WeatherEntity
import com.example.washweather.data.db.entity.NoteEntity

@Database(
        entities = [WeatherEntity::class, NoteEntity::class],
        version = 2
)
abstract class ForecastDataBase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDAO

    companion object {
        @Volatile
        private var instance: ForecastDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        ForecastDataBase::class.java, "wash_weather.db")
                        .fallbackToDestructiveMigration()
                        .build()
    }
}