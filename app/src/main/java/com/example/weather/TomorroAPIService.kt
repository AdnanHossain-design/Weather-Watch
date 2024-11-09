package com.example.weather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Data classes representing the JSON response structure
data class WeatherForecastResponse(
    val location: Location,
    val forecast: List<Forecast>
)

data class Location(
    val lat: Double,
    val lon: Double,
    val city: String,
    val country: String
)

data class Forecast(
    val timestamp: String,
    val temperature: Double,
    val precipitation: Double
)

// Retrofit service interface for Tomorrow.io API
interface TomorrowApiService {
    @GET("v4/weather/forecast")
    suspend fun getWeatherForecast(
        @Query("location") location: String,
        @Query("apikey") apiKey: String
    ): WeatherForecastResponse
}

// Singleton object for Retrofit instance
object RetrofitInstance {
    private const val BASE_URL = "https://api.tomorrow.io/"

    val api: TomorrowApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TomorrowApiService::class.java)
    }
}
