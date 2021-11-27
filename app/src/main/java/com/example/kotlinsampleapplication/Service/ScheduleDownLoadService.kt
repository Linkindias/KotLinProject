package com.example.kotlinsampleapplication.Service

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import com.example.base.Common
import com.example.base.Common.Companion.mediaDownloadApi
import com.example.base.Common.Companion.mediaScheduleApi
import com.example.base.Common.Companion.sdcardDownLoad
import com.example.base.Common.Companion.sdfJson
import com.example.base.HttpService
import com.example.kotlinsampleapplication.ViewModel.VideoDetial
import com.example.kotlinsampleapplication.ViewModel.VideoInfo
import com.google.gson.Gson
import java.io.File
import java.util.*


class ScheduleDownLoadService : IntentService("single") {
    companion object {
        const val downLoadflag = 1
        var downloadErrorList: MutableList<String> = mutableListOf()
    }

    var tag: String = "FileService"
    var receiver: ResultReceiver? = null
    var schedulsList: List <VideoDetial> = listOf()

    override fun onHandleIntent(intent: Intent?) {

        try {
            getServerSchedules()

            if (schedulsList.isNotEmpty()){
                deleteLocalFiles()

                setFileNamePathProperty()

                getServerFiles()
            }
        } catch (ex: Exception) {
            Log.e(tag, ex.message.toString())
            return
        }

        transefarStringToDateOfSchedule()

        if (schedulsList.isNotEmpty()) {
            sendNotifyScheduleComplate(intent)
        }
    }

    private fun sendNotifyScheduleComplate(intent: Intent?) {
        receiver = intent?.getParcelableExtra("receiver") as ResultReceiver?
        val bundle = Bundle()
        bundle.putParcelableArrayList("schedules", schedulsList as ArrayList<VideoDetial>)
        receiver!!.send(downLoadflag, bundle)
    }

    private fun transefarStringToDateOfSchedule() {
        schedulsList.forEach {
            it.sDate = sdfJson.parse(it.startDate)
            it.eDate = sdfJson.parse(it.endDate)
        }
    }

    private fun getServerFiles() {
        var distinctFiles = schedulsList.distinctBy { it.fileName }
        distinctFiles.forEach {
            val file = File(it.path)
            if (!file.exists()) {
                var result = HttpService().sendGetFile(mediaDownloadApi + it.fileName, file)

                if (result != 200) downloadErrorList.add(it.fileName)
            }
        }
    }

    private fun setFileNamePathProperty() {
        schedulsList.forEach {
            var fileName = it.fileName
            it.path = sdcardDownLoad.path + "/" + fileName
        }
    }

    private fun deleteLocalFiles() {
        sdcardDownLoad.listFiles().forEach {
            it.delete()
        }
    }

    private fun getServerSchedules() {
        var schedule = HttpService().sendGet(mediaScheduleApi)
        if (schedule != null) {

            var video = Gson().fromJson(schedule.toString(), VideoInfo::class.java)
            if (video != null && video.videos?.size!! > 0)
                schedulsList = video.videos!!.toList()
        }
    }

    public fun reDownloadFile() {
        var downloadList = downloadErrorList.distinct().toMutableList()
        downloadErrorList.clear()
        downloadList.forEach {
            val file = File( Common.sdcardDownLoad.path,it)
            if(!file.exists()){
                var result = HttpService().sendGetFile(Common.mediaDownloadApi + it, file)

                if (result != 200) downloadErrorList.add(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

