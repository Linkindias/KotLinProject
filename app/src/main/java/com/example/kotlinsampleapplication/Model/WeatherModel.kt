package com.example.kotlinsampleapplication.Model
import com.google.gson.annotations.SerializedName

data class WeatherModel(
    @SerializedName("success") val success: Boolean,
    @SerializedName("result") val result: ResultModel,
    @SerializedName("records") val records: RecordModel,
)
