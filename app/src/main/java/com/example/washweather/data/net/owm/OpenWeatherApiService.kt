package com.example.washweather.data.net.owm

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.washweather.data.net.interceptors.ConnectivityInterceptor
import com.example.washweather.data.net.owm.owfResponse.OWForecastResponse

const val API_KEY = "8c809aface8967d960a0bec0db127446" // API 9a43a50399281972005a4e0935e473fc
const val BASE_URL = "https://api.openweathermap.org/data/2.5/"


interface OpenWeatherApiService {

    @GET("forecast/daily")
    fun getForecastWeatherAsync(
            @Query("q") location: String,
            @Query("units") units: String,
            @Query("lang") language: String,
            @Query("cnt") cnt: String = "16"): Deferred<Response<OWForecastResponse>>

    companion object {
        fun getWeatherApi(
                connectivityInterceptor: ConnectivityInterceptor
        ): OpenWeatherApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter("appId", API_KEY)
                        .build()
                val request = chain.request()
                        .newBuilder()
                        .url(url)
                        .build()
                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(requestInterceptor)
                    .addInterceptor(connectivityInterceptor)
                    .build()
            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(OpenWeatherApiService::class.java)
        }
    }
}