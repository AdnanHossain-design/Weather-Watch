package com.example.weather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Data classes representing the JSON response structure
data class WeatherResponse(
    val location: Location,
    val current: CurrentWeather // Updated to use 'current' based on Weatherstack's response structure
)

data class Location(
    val name: String,
    val country: String,
    val region: String,
    val lat: Double,
    val lon: Double,
    val timezone_id: String,
    val localtime: String,
    val localtime_epoch: Long,
    val utc_offset: String
)

data class CurrentWeather(
    val temperature: Double,
    val precipitation: Double,
    val weather_descriptions: List<String>
)

// Retrofit service interface for Weatherstack API
interface WeatherstackApiService {
    @GET("current")
    suspend fun getWeather(
        @Query("access_key") apiKey: String,
        @Query("query") location: String
    ): WeatherResponse
}

// Singleton object for Retrofit instance
object RetrofitInstance {
    private const val BASE_URL = "https://api.weatherstack.com/"

    val api: WeatherstackApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherstackApiService::class.java)
    }
}