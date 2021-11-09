package com.example.kotlinsampleapplication.Model

import com.google.gson.annotations.SerializedName

data class ParameterModel(
    @SerializedName("parameterName") val parameterName: String,
    @SerializedName("parameterValue") val parameterValue: String,
    @SerializedName("parameterUnit") val parameterUnit: String,
)
