package com.example.kotlinsampleapplication.MediaDomain

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import com.example.kotlinsampleapplication.Base.Companion.sdcardDownLoad
import com.example.kotlinsampleapplication.Base.Companion.sdf
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
        const val downLoadflag = 1
        var downloadErrorList: MutableList<String> = mutableListOf()
    }

    var tag: String = "FileService"
    var receiver: ResultReceiver? = null
    var schedulsList: List <VideoDetial> = listOf()

    override fun onHandleIntent(intent: Intent?) {

        if (schedulsList.isEmpty()) {
            try {
                var schedule = HttpService().sendGet(videoScheduleApi)
                if (schedule != null) {

                    var video = Gson().fromJson(schedule.toString(), VideoInfo::class.java)
                    if(video != null && video.videos?.size!! > 0)
                        schedulsList = video.videos!!.toList()
                }

                if (schedulsList.isNotEmpty()){
                    sdcardDownLoad.listFiles().forEach {
                        it.delete()
                    }

                    schedulsList.forEach {
                        var fileName = it.fileName
                        it.path = sdcardDownLoad.path + "/" + fileName
                    }

                    var distinctFiles = schedulsList.distinctBy { it.fileName }
                    distinctFiles.forEach {
                        val file = File(it.path)
                        if(!file.exists()){
                            var result = HttpService().sendGetFile(videoDownloadApi + it.fileName, file)

                            if (result != 200) downloadErrorList.add(it.fileName)
                        }
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
            receiver = intent?.getParcelableExtra("receiver") as ResultReceiver?
            val bundle = Bundle()
            bundle.putParcelableArrayList("schedules", schedulsList as ArrayList<VideoDetial>)
            receiver!!.send(downLoadflag, bundle)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

