package com.example.weather

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val countries = listOf("United States", "Canada", "Mexico", "United Kingdom", "Germany")
        val countryDropDown = findViewById<Spinner>(R.id.CountryList)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countryDropDown.adapter = adapter

        val cityMap = mapOf(
            "United States" to listOf("New York", "Chicago", "Los Angeles"),
            "Canada" to listOf("Toronto", "Ottawa"),
            "Mexico" to listOf("Mexico City"),
            "United Kingdom" to listOf("London", "Manchester"),
            "Germany" to listOf("Munich", "Berlin")
        )

        countryDropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCountry = parentView?.getItemAtPosition(position).toString()
                val cities = cityMap[selectedCountry] ?: emptyList()

                Log.d("SelectedCountry", "Selected country: $selectedCountry")
                Log.d("Cities", "Cities: $cities")

                val cityAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, cities)
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                val cityDropDown = findViewById<Spinner>(R.id.CityList)
                cityDropDown.adapter = cityAdapter

                cityDropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parentView: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val selectedCity = parentView?.getItemAtPosition(position).toString()
                        fetchWeatherForecast(selectedCountry, selectedCity)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        // Optionally handle no city selection
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Optionally handle no selection
            }
        }
    }

    private fun fetchWeatherForecast(country: String, city: String) {
        val location = "$city,$country"
        val apiKey = "d4d7124ad61acedc9bab4df998c8fca6" // Replace with your actual Weatherstack API key

        lifecycleScope.launch {
            try {
                // Make the API call to get the weather forecast
                val response = RetrofitInstance.api.getWeather(apiKey, location)

                // Assuming the response is a WeatherResponse object
                response?.let {
                    displayWeatherForecast(it.current)
                } ?: run {
                    Toast.makeText(this@MainActivity, "No forecast data available", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Error fetching weather forecast: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun displayWeatherForecast(currentWeather: CurrentWeather) {
        val weatherTextView = findViewById<TextView>(R.id.weatherStatusTextView)
        val temperatureText = "Temperature: ${currentWeather.temperature}°C"
        val precipitationText = "Precipitation: ${currentWeather.precipitation} mm"
        val descriptionText = "Description: ${currentWeather.weather_descriptions.joinToString()}"

        weatherTextView.text = "$temperatureText\n$precipitationText\n$descriptionText"
    }
}
