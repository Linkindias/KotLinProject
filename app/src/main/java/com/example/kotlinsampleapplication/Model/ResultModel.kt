package com.example.kotlinsampleapplication.Model

import com.google.gson.annotations.SerializedName

data class ResultModel(
    @SerializedName("resource_id") val resource_id: String,
    @SerializedName("fields") val fields: Array<FieldModel>
)
