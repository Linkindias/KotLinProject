package com.example.kotlinsampleapplication.Model

import com.google.gson.annotations.SerializedName

data class FieldModel(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
)
