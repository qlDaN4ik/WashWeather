package com.example.washweather.data.net.owm.owfResponse


data class Temp(
        val day: Double = 0.0,
        val eve: Double = 0.0,
        val max: Double = 0.0,
        val min: Double = 0.0,
        val morn: Double = 0.0,
        val night: Double = 0.0
)