package com.example.washweather

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.android.x.androidXModule
import org.kodein.di.*
import com.example.washweather.data.db.ForecastDataBase
import com.example.washweather.data.db.WeatherDAO
import com.example.washweather.data.net.owm.OpenWeatherApiService
import com.example.washweather.data.net.owm.OWNetworkDataSourceImpl
import com.example.washweather.data.net.interceptors.ConnectivityInterceptor
import com.example.washweather.data.net.interceptors.ConnectivityInterceptorImpl
import com.example.washweather.data.repository.WeatherRepository
import com.example.washweather.data.repository.WeatherRepositoryImpl
import com.example.washweather.ui.forecast.ForecastWeatherViewModelFactory
import com.example.washweather.ui.weather.CurrentWeatherViewModelFactory

class WashWeather : Application(), DIAware {
    override val di = DI.lazy {
        import(androidXModule(this@WashWeather))

        bind<ForecastDataBase>() with singleton { ForecastDataBase(instance()) }
        bind<WeatherDAO>() with singleton { instance<ForecastDataBase>().weatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind<OpenWeatherApiService>() with singleton { OpenWeatherApiService.getWeatherApi(instance()) }
        bind<OWNetworkDataSourceImpl>() with singleton { OWNetworkDataSourceImpl(instance(), instance()) }
        bind<WeatherRepository>() with singleton { WeatherRepositoryImpl(instance(), instance()) }
        bind() from provider { ForecastWeatherViewModelFactory(instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance()) }

    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

}