package com.example.washweather.data.net.owm.owfResponse


import com.google.gson.annotations.SerializedName

data class City(
        val country: String = "",
        @SerializedName("geoname_id")
        val geonameId: Int = 0,
        val iso2: String = "",
        val lat: Double = 0.0,
        val lon: Double = 0.0,
        val name: String = "",
        val population: Int = 0,
        val type: String = ""
)