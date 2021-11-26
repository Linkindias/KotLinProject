package com.example.kotlinsampleapplication.Model

import com.google.gson.annotations.SerializedName

data class MediaCModel(
    @SerializedName("info") val info: Array<MediaCUModel>,
)
