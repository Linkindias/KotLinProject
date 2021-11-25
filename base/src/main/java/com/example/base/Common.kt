package com.example.base

import android.os.Environment
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class Common {
    companion object {
        val sdfJson = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val domainUrl = "http://10.168.18.61"
        val mediaScheduleApi = "$domainUrl/webapplication/api/video/FileSchedule"
        val mediaDownloadApi = "$domainUrl/webapplication/api/video/DownLoadFile?fileName="
        val sdcardDownLoad = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val jsonType = "application/json"

        @TypeConverter
        @JvmStatic
        fun stringToDate(value: String): Date {
            return sdf.parse(value)
        }

        @TypeConverter
        @JvmStatic
        fun dateToString(value: Date): String {
            return sdf.format(value)
        }
    }
}