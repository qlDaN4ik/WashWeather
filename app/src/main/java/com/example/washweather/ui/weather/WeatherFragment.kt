package com.example.washweather.ui.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.weather_fragment.*
import org.kodein.di.*
import org.kodein.di.android.x.di
import com.example.washweather.R
import com.example.washweather.data.db.entity.WeatherEntity
import com.example.washweather.ui.forecast.ForecastWeatherViewModel
import com.example.washweather.ui.forecast.ForecastWeatherViewModelFactory
import com.example.washweather.ui.forecast.RWForecastAdapter
import kotlin.math.roundToInt

class WeatherFragment : Fragment(R.layout.weather_fragment), DIAware {
    override val di by di()
    private val currentWeatherViewModelFactory: CurrentWeatherViewModelFactory by instance()
    private val forecastWeatherViewModelFactory: ForecastWeatherViewModelFactory by instance()
    private lateinit var rwForecastAdapter: RWForecastAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        initViewModels()
    }

    private fun initViews() {
        rwForecastAdapter = RWForecastAdapter {
            val msg = if (it.advices.needWash)
                getString(R.string.wash)
            else
                getString(R.string.not_wash)
            Snackbar.make(rwForecasts, msg, Snackbar.LENGTH_SHORT).show()
        }
        with(rwForecasts) {
            adapter = rwForecastAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    private fun initViewModels() {
        val currentWeatherViewModel = ViewModelProvider(this, currentWeatherViewModelFactory).get(CurrentWeatherViewModel::class.java)
        currentWeatherViewModel.getWeather().observe(viewLifecycleOwner, {
            if (it != null) {
                fillWeatherCard(it)
            }
        })

        val forecastWeatherViewModel = ViewModelProvider(this, forecastWeatherViewModelFactory).get(ForecastWeatherViewModel::class.java)
        forecastWeatherViewModel.getForecastWeather().observe(viewLifecycleOwner, {
            if (it != null) {
                rwForecastAdapter.addItems(it.dropLast(8))
            }
        })

        currentWeatherViewModel.getProgressState().observe(viewLifecycleOwner, {
            if (it) {
                progress_Bar.visibility = VISIBLE
            } else {
                progress_Bar.visibility = GONE
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun fillWeatherCard(entity: WeatherEntity) {
        with(entity) {
            textTemp.text = temp.toString() + "\u2103"
            textHumidity.text = "$humidity%"
            textBarometer.text = "$pressure мПа"
            textSpeedWind.text = "${wind.speed} м/с"
            textWindDir.text = getString(getWindRes(wind.deg))
            weatherIcon.setImageResource(icon)
            tw_city_name.text = location.locationName
        }

        with(entity.advices) {
            if (needGlasses) glasses_icon.visibility = VISIBLE else glasses_icon.visibility = GONE
            if (needUmbrella) umbrella_icon.visibility = VISIBLE else umbrella_icon.visibility = GONE
            if (needWash) car_wash_icon.visibility = VISIBLE else car_wash_icon.visibility = GONE
            if (needLight) car_light_icon.visibility = VISIBLE else car_light_icon.visibility = GONE
            if (needWear) hat_icon.visibility = VISIBLE else hat_icon.visibility = GONE
        }
    }

    private fun getWindRes(direction: Double): Int {
        val dir = (direction % 360 / 45).roundToInt()
        when (dir % 16) {
            0 -> return R.string.wi_wind_north
            1 -> return R.string.wi_wind_north_east
            2 -> return R.string.wi_wind_east
            3 -> return R.string.wi_wind_south_east
            4 -> return R.string.wi_wind_south
            5 -> return R.string.wi_wind_south_west
            6 -> return R.string.wi_wind_west
            7 -> return R.string.wi_wind_north_west
            8 -> return R.string.wi_wind_north
        }
        return R.string.wi_wind_north
    }
}