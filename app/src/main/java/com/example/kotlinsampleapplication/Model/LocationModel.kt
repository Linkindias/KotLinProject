package com.example.kotlinsampleapplication.Model

import com.google.gson.annotations.SerializedName

data class LocationModel(
    @SerializedName("locationName") val locationName: String,
    @SerializedName("weatherElement") val weatherElement: Array<WeatherElement>,
)
