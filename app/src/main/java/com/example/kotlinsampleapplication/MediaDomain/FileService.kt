package com.example.kotlinsampleapplication.MediaDomain

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import com.example.kotlinsampleapplication.Base.Companion.sdcardDownLoad
import com.example.kotlinsampleapplication.Base.Companion.sdfJson
import com.example.kotlinsampleapplication.Base.Companion.videoDownloadApi
import com.example.kotlinsampleapplication.Base.Companion.videoScheduleApi
import com.example.kotlinsampleapplication.HttpService
import com.example.kotlinsampleapplication.ViewModel.VideoDetial
import com.example.kotlinsampleapplication.ViewModel.VideoInfo
import com.google.gson.Gson
import java.io.File
import java.util.*


class FileService : IntentService("single") {
    companion object {
        const val playflag = 1
        const val stopflag = 2
    }

    var tag: String = "FileService"
    var receiver: ResultReceiver? = null
    var schedulsList: List <VideoDetial> = listOf()

    override fun onHandleIntent(intent: Intent?) {
        val uuid = UUID.randomUUID()
        Log.i("FileService", "start:$uuid")

        if (schedulsList.isEmpty()) {
            try {
                var schedule = HttpService().sendGet(videoScheduleApi)
                if (schedule != null) {

                    var video = Gson().fromJson(schedule.toString(), VideoInfo::class.java)
                    if(video != null && video.videos?.size!! > 0)
                        schedulsList = video.videos!!.toList()
                }

                if (schedulsList.isNotEmpty()){
                    schedulsList.forEach {
                        var fileName = it.fileName
                        var file = File(sdcardDownLoad, fileName)
                        if (!file.exists()) {
                            HttpService().sendGetFile(videoDownloadApi + fileName, file)
                        }
                        it.path = file.path
                    }
                }
            } catch (ex: Exception) {
                Log.e(tag, ex.message.toString())
            }
        }

        schedulsList.forEach {
            it.sDate = sdfJson.parse(it.startDate)
            it.eDate = sdfJson.parse(it.endDate)
        }

        if (schedulsList.isNotEmpty()) {
            try {
                var dtNow = Date()
                var currentStartSchedule: VideoDetial? = null
                var nextStartSchedule: VideoDetial? = null

                schedulsList.forEach {
                    if (it.sDate!! <= dtNow && dtNow <= it.eDate) currentStartSchedule = it

                    if(dtNow < it.sDate && dtNow < it.eDate) nextStartSchedule = it
                }

                receiver = intent?.getParcelableExtra("receiver") as ResultReceiver?
                val bundle = Bundle()

                if (currentStartSchedule != null ) {
                    bundle.putString("currentPath", currentStartSchedule!!.path)
                    bundle.putString("currentType", currentStartSchedule!!.type)
                    bundle.putString("stopDate", currentStartSchedule!!.endDate)
                    bundle.putParcelableArrayList("schedules", schedulsList as ArrayList<VideoDetial>);
                    receiver!!.send(playflag, bundle)
                } else if (nextStartSchedule != null ) {
                    bundle.putString("currentPath", nextStartSchedule!!.path)
                    bundle.putString("currentType", nextStartSchedule!!.type)
                    bundle.putString("startDate", nextStartSchedule!!.startDate)
                    bundle.putParcelableArrayList("schedules", schedulsList as ArrayList<VideoDetial>);
                    receiver!!.send(stopflag, bundle)
                }
            } catch (ex: Exception) {
                Log.e(tag, ex.message.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

