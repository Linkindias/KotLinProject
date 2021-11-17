package com.example.kotlinsampleapplication

import android.os.Environment
import java.text.SimpleDateFormat

class Base {
    companion object {
        val sdfJson = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val domainUrl = "http://10.168.18.61"
        val videoScheduleApi = "$domainUrl/webapplication/api/video/FileSchedule"
        val videoDownloadApi = "$domainUrl/webapplication/api/video/DownLoadFile?fileName="
        val sdcardDownLoad = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    }
}