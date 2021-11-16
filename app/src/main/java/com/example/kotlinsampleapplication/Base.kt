package com.example.kotlinsampleapplication

import java.text.SimpleDateFormat

class Base {
    companion object {
        val sdfJson = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    }
}