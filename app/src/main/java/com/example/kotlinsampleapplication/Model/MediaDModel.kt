package com.example.kotlinsampleapplication.Model

import com.google.gson.annotations.SerializedName

data class MediaDModel(
    @SerializedName("fileName") val fileName: String
)