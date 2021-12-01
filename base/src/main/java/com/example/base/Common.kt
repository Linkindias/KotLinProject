package com.example.base

import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        val successFlag: String = "{ 'msg': 'success' }"
        val errorFlag: String = "{ 'msg': 'error' }"

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

        @JvmStatic
        fun hideBar(activity: AppCompatActivity) {
            val flags = View.SYSTEM_UI_FLAG_LOW_PROFILE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            activity.window?.decorView?.systemUiVisibility = flags
            activity.supportActionBar?.hide()
        }
    }
}