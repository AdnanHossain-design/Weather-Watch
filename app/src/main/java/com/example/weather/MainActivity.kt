package com.example.weather

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Your Composable UI goes here
        }

        // Fetch weather forecast on activity creation
        fetchWeatherForecast()
    }

    private fun fetchWeatherForecast() {
        val location = "42.3478,-71.0466" // Example coordinates for Boston, MA
        val apiKey = "GQAyP33k6fOc9d7484Qae4rPYTMcmffo" // Your API key

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getWeatherForecast(location, apiKey)
                // Handle the successful response, e.g., update your UI with the weather data
                Toast.makeText(this@MainActivity, "Forecast fetched successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle the error case, such as showing a message to the user
                Toast.makeText(this@MainActivity, "Error fetching weather forecast: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
