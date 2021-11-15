package com.example.kotlinsampleapplication.MediaDomain

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.ResultReceiver
import android.util.Log
import com.example.kotlinsampleapplication.HttpService
import com.example.kotlinsampleapplication.ViewModel.VideoDetial
import com.example.kotlinsampleapplication.ViewModel.VideoInfo
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class FileService : IntentService("") {
    companion object {
        const val playflag = 1
        const val stopflag = 2
    }

    var tag: String = "FileService"
    var receiver: ResultReceiver? = null
    var schedulsList: List <VideoDetial> = listOf()

    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    override fun onHandleIntent(intent: Intent?) {
        Log.i("FileService","start")

        var loadCount = 0

        if (schedulsList.isEmpty()) {

            try {
                var schedule = HttpService().sendGet("http://10.168.18.61/webapplication/api/video/FileSchedule")
                if (schedule != null) {

                    var video = Gson().fromJson(schedule.toString(), VideoInfo::class.java)
                    if(video != null && video.videos?.size!! > 0)
                        schedulsList = video.videos!!.toList()
                }

                if (schedulsList.isNotEmpty()){
//                    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"*.*").delete()

                    schedulsList.forEach {
                        var fileName = it.fileName
                        var file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
                        var result = HttpService().sendGetFile("http://10.168.18.61/webapplication/api/video/DownLoadFile?fileName=$fileName", file)
                        it.path = file.path
                        if (result == 200) loadCount++
                    }
                }
            } catch (ex: Exception) {
                Log.e(tag, ex.message.toString())
            }
        }

        schedulsList.forEach {
            it.sDate = sdf.parse(it.startDate)
            it.eDate = sdf.parse(it.endDate)
        }


        if (loadCount == schedulsList.size) {

            //在深的可以抽findSchedule class to 現在、下個排程
            var currentStartSchedule = schedulsList.find { it.sDate?.compareTo(Date())!! < 0 && it.eDate?.compareTo(Date())!! > 0 }
            var nextStartSchedule = schedulsList.find { it.sDate?.compareTo(Date())!! > 0 }
            receiver = intent?.getParcelableExtra("receiver") as ResultReceiver?
            val bundle = Bundle()

            if (currentStartSchedule != null) {
                Log.i(tag, "start < now < end")
                bundle.putString("currentPath", currentStartSchedule.path)
                bundle.putString("currentType", currentStartSchedule.type)
                bundle.putString("stopDate", currentStartSchedule.endDate)
                bundle.putParcelableArrayList("schedules", schedulsList as ArrayList<VideoDetial>);
                receiver!!.send(playflag, bundle)
            } else if (nextStartSchedule != null) {
                Log.i(tag, "start > now")
                bundle.putString("currentPath", nextStartSchedule.path)
                bundle.putString("currentType", nextStartSchedule.type)
                bundle.putString("startDate", nextStartSchedule.startDate)
                bundle.putParcelableArrayList("schedules", schedulsList as ArrayList<VideoDetial>);
                receiver!!.send(stopflag, bundle)
            }
            Log.i(tag, "not schedules")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

