package com.example.kotlinsampleapplication.Model

import com.google.gson.annotations.SerializedName

data class MediaModel(
    @SerializedName("path") val path: String,
    @SerializedName("type") val type: String,
    @SerializedName("fileName") val fileName: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
)
