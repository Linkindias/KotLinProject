package com.example.kotlinsampleapplication.MediaDomain

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.ResultReceiver
import android.util.Log
import com.example.kotlinsampleapplication.Base.Companion.sdf
import com.example.kotlinsampleapplication.Base.Companion.sdfJson
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
        Log.i("FileService","start")

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
                        if (!file.exists()) HttpService().sendGetFile("http://10.168.18.61/webapplication/api/video/DownLoadFile?fileName=$fileName", file)
                        it.path = file.path
                    }
                }
            } catch (ex: Exception) {
                Log.e(tag, ex.message.toString())
            }
        }

        Log.i(tag,"s:" + schedulsList.size.toString())
        schedulsList.forEach {
//            Log.i(tag,"s:" + sdf.format(sdfJson.parse(it.startDate)))
//            Log.i(tag,"e:" + sdf.format(sdfJson.parse(it.endDate)))
//            Log.i(tag, " p:" + it.path + " n:" + sdf.format(Date()))

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
                    Log.i(tag, "start < now < end")
                    bundle.putString("currentPath", currentStartSchedule!!.path)
                    bundle.putString("currentType", currentStartSchedule!!.type)
                    bundle.putString("stopDate", currentStartSchedule!!.endDate)
                    bundle.putParcelableArrayList("schedules", schedulsList as ArrayList<VideoDetial>);
                    receiver!!.send(playflag, bundle)
                } else if (nextStartSchedule != null ) {
                    Log.i(tag, "start > now")
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

