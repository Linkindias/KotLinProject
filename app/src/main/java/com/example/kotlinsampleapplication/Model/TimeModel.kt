package com.example.kotlinsampleapplication.Model

import com.google.gson.annotations.SerializedName

data class TimeModel(
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("parameter") val parameter: ParameterModel,
)
