package com.example.kotlinsampleapplication.Model

import com.google.gson.annotations.SerializedName

data class WeatherElement(
    @SerializedName("elementName") val elementName: String,
    @SerializedName("time") val time: Array<TimeModel>,
)
