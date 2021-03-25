package com.example.washweather.data.net.owm

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.example.washweather.R
import com.example.washweather.data.db.entity.Advices
import com.example.washweather.data.db.entity.Location
import com.example.washweather.data.db.entity.WeatherEntity
import com.example.washweather.data.db.entity.Wind
import com.example.washweather.internal.NoConnectivityException
import com.example.washweather.data.net.owm.owfResponse.OWForecastResponse
import com.example.washweather.data.net.WeatherNetworkDataSource

class OWNetworkDataSourceImpl(
        private val openWeatherApiService: OpenWeatherApiService,
        val context: Context
) : WeatherNetworkDataSource {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    override suspend fun getForecastWeather(location: String, units: String, language: String): List<WeatherEntity> {
        var forecast: MutableList<WeatherEntity> = mutableListOf()
        try {
            val fetchedForecast = openWeatherApiService
                    .getForecastWeatherAsync(location, units, language)
                    .await()
            return if (fetchedForecast.isSuccessful) {
                forecast = forecastMapping(fetchedForecast.body()!!)
                forecast
            } else forecast
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
        return forecast
    }

    private fun forecastMapping(response: OWForecastResponse): MutableList<WeatherEntity> {
        val limitDayFromPref = preferences.getString(context.resources.getString(R.string.pref_limit_key), "2")!!
        val minTempFromPref = preferences.getString(context.resources.getString(R.string.pref_min_temp_key), "-30")!!
        val forecast: MutableList<WeatherEntity> = mutableListOf()
        response.list.indices.forEach { id ->
            val ids = mutableListOf<Int>()
            response.list[id].weather.forEach {
                ids.add(it.id)
            }
            var checkDayLimit = true
            for (i in (id + 1)..(id + limitDayFromPref.toInt())){
                if (i < 16){
                    response.list[i].weather.forEach {
                        if (it.id in 200..622)
                            checkDayLimit = false
                    }
                }
            }
            val checkMinTemp = response.list[id].temp.day.toInt() >= minTempFromPref.toInt()
            forecast.add(WeatherEntity(
                    id,
                    response.list[id].humidity,
                    response.list[id].pressure.toInt(),
                    response.list[id].temp.day.toInt(),
                    response.list[id].temp.max.toInt(),
                    response.list[id].temp.min.toInt(),
                    Wind(response.list[id].deg, response.list[id].speed),
                    response.list[id].weather[0].description,
                    getAdvices(ids, checkDayLimit, checkMinTemp),
                    getWeatherPicture(response.list[id].weather[0].icon),
                    Location(response.city.lat, response.city.lon, response.city.name),
                    response.list[id].dt.toLong()
            ))
        }
        return forecast
    }

    private fun getAdvices(ids: MutableList<Int>, checkDayLimit: Boolean, checkMinTemp: Boolean): Advices {
        var needGlasses = false
        var needUmbrella = false
        var needWash = checkDayLimit and checkMinTemp
        var needLight = false
        var needWear = false
        ids.forEach {
            when (it) {
                in 200..531 -> {
                    needUmbrella = true
                    needWash = false
                }
                in 600..622 -> {
                    needWear = true
                    needWash = false
                }
                in 701..781 -> {
                    needLight = true
                }
                800 -> {
                    needGlasses = true
                }
            }
        }
        return Advices(needGlasses, needUmbrella, needWash, needLight, needWear)

    }

    private fun getWeatherPicture(icon: String): Int {
        when (icon) {
            "01d" -> return R.drawable.ic_weather_icons_02
            "01n" -> return R.drawable.ic_weather_icons_02
            "02d" -> return R.drawable.ic_weather_icons_05
            "02n" -> return R.drawable.ic_weather_icons_05
            "03d" -> return R.drawable.ic_weather_icons_03
            "03n" -> return R.drawable.ic_weather_icons_03
            "04d" -> return R.drawable.ic_weather_icons_04
            "04n" -> return R.drawable.ic_weather_icons_04
            "09d" -> return R.drawable.ic_weather_icons_12
            "09n" -> return R.drawable.ic_weather_icons_12
            "10d" -> return R.drawable.ic_weather_icons_08
            "10n" -> return R.drawable.ic_weather_icons_08
            "11d" -> return R.drawable.ic_weather_icons_06
            "11n" -> return R.drawable.ic_weather_icons_06
            "13d" -> return R.drawable.ic_weather_icons_10
            "13n" -> return R.drawable.ic_weather_icons_10
            "50d" -> return R.drawable.ic_weather_icons_09
            "50n" -> return R.drawable.ic_weather_icons_09
            else -> return R.drawable.ic_weather_icons_02
        }
    }
}