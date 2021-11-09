package com.example.kotlinsampleapplication.Model

import com.google.gson.annotations.SerializedName

data class RecordModel(
    @SerializedName("datasetDescription") val datasetDescription: String,
    @SerializedName("location") val location: Array<LocationModel>,
)
