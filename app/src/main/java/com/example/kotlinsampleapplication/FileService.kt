package com.example.kotlinsampleapplication

import android.app.IntentService
import android.app.SearchManager.QUERY
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.ResultReceiver
import android.util.Log
import com.example.kotlinsampleapplication.ViewModel.VideoDetial
import com.example.kotlinsampleapplication.ViewModel.VideoInfo
import com.google.gson.Gson


class FileService : IntentService("FileService") {
    var receiver: ResultReceiver? = null
    var schedulsList: MutableList<VideoDetial> = mutableListOf()
    var tag: String = "FileService"

    override fun onHandleIntent(intent: Intent?) {
        Log.i("FileService","start")

        if (schedulsList.size == 0) {
            var path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
            var videoD1 = VideoDetial()  //move sqllite
            videoD1.fileName = "movie.mp4"
            videoD1.type = "video"
            videoD1.startDate = "2021-11-11 14:00:00"
            videoD1.endDate = "2021-11-11 14:30:00"
            videoD1.path = path + "/" + videoD1.fileName
            schedulsList.add(videoD1)
            var videoD2 = VideoDetial()
            videoD2.fileName = "movie.mp4"
            videoD2.type = "video"
            videoD2.startDate = "2021-11-11 14:00:00"
            videoD2.endDate = "2021-11-11 14:30:00"
            videoD2.path = path + "/" + videoD2.fileName
            schedulsList.add(videoD2)

            var result = HttpService().sendGet("http://10.168.18.61/webapplication/api/video/FileSchedule")
            if (result != null) {
                try {
                    var gson = Gson()
                    var video = gson.fromJson(result.toString(), VideoInfo::class.java)
                    if(video != null && video.videos?.size!! > 0) {
                        video.videos!!.forEach {
                            schedulsList.add(it)
                        }
                    }
                }
                catch (ex: Exception) {
                    Log.i(tag, ex.message.toString())
                }
                Log.i(tag, schedulsList.size.toString())
            }
        }

        receiver = intent?.getParcelableExtra("receiver") as ResultReceiver?

        val bundle = Bundle()
        bundle.putString("currentPath", schedulsList[0].path)
        bundle.putString("currentType", schedulsList[0].type)
        receiver!!.send(1, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}