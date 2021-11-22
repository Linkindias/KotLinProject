package com.example.kotlinsampleapplication.MediaDomain

import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.util.Log
import android.widget.ImageView
import android.widget.VideoView
import com.example.kotlinsampleapplication.Base
import com.example.kotlinsampleapplication.Base.Companion.sdf
import com.example.kotlinsampleapplication.HttpService
import com.example.kotlinsampleapplication.MediaActivity
import com.example.kotlinsampleapplication.Service.ScheduleDownLoadService.Companion.downloadErrorList
import com.example.kotlinsampleapplication.ViewModel.VideoDetial
import java.io.File
import java.util.*

class MediaScheduleService {
    val tag: String = "MediaScheduleService"

    var activity: MediaActivity? = null
    var mediaPlayRunnable: MediaPlayRunnable = MediaPlayRunnable()
    var mediaStopRunnable: MediaStopRunnable = MediaStopRunnable()
    var timer: Timer? = null

    var mediaSchedules = ArrayList<VideoDetial>()

    constructor(activity: MediaActivity, video: VideoView, img: ImageView, sound: MediaPlayer, defaultDrawable: Drawable?) {
        this.activity = activity
        mediaPlayRunnable.setVideoControl(video!!, img!!, sound!!)
        mediaStopRunnable.setVideoControl(video!!, img!!, sound!!, defaultDrawable!!)
    }

    fun getMediaSchedule(schedules: ArrayList<VideoDetial>) {
        mediaSchedules = schedules

        try {
            var dtNow = Date()
            var currentStartSchedule: VideoDetial? = null
            var nextStartSchedule: VideoDetial? = null

            var indexflag = -1;
            mediaSchedules.forEachIndexed { index, it ->
                if (indexflag == -1 && it.sDate!! <= dtNow && dtNow <= it.eDate) {
                    currentStartSchedule = it
                    indexflag = index

                } else if(indexflag == -1 && dtNow <= it.sDate && dtNow <= it.eDate) {
                    nextStartSchedule = it
                    indexflag = index
                }
            }

            if (currentStartSchedule != null ) {
                mediaPlayRunnable.setMediaPathType(currentStartSchedule!!.path, currentStartSchedule!!.type)
                activity?.runOnUiThread(mediaPlayRunnable)
                findCurrentScheduleToEndDate(indexflag)

            } else if (nextStartSchedule != null ) {
                activity?.runOnUiThread(mediaStopRunnable)
                findNextScheduleToStartDate(indexflag)
            }
            else
                activity?.runOnUiThread(mediaStopRunnable)

        } catch (ex: Exception) {
            Log.e(tag, ex.message.toString())
        }
    }

    fun findCurrentScheduleToEndDate(currentIndex: Int) {
        if (mediaSchedules.size > currentIndex) {
            val endDuration: Long = mediaSchedules[currentIndex].eDate!!.time - Date().time
            Log.i(tag, "e:" + sdf.format(mediaSchedules[currentIndex].eDate) + " d:" + sdf.format(Date()))

            timer = Timer()
            timer?.schedule( object : TimerTask() {
                override fun run() {
                    activity?.runOnUiThread(mediaStopRunnable)
                    timer?.cancel()
                    timer?.purge()
                    timer = null

                    findNextScheduleToStartDate(currentIndex + 1)
                }
            }, endDuration)
        }
        else
            activity?.runOnUiThread(mediaStopRunnable)
    }

    fun findNextScheduleToStartDate(nextIndex: Int) {
        if (mediaSchedules.size > nextIndex) {
            val startDuration: Long = mediaSchedules[nextIndex].sDate!!.time - Date().time
            Log.i(tag, "s:" + sdf.format(mediaSchedules[nextIndex].sDate) + " d:" + sdf.format(Date()))

            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    mediaPlayRunnable.setMediaPathType(mediaSchedules[nextIndex].path, mediaSchedules[nextIndex].type)
                    activity?.runOnUiThread(mediaPlayRunnable)
                    timer?.cancel()
                    timer?.purge()
                    timer = null

                    findCurrentScheduleToEndDate(nextIndex)
                }
            }, startDuration)
        }
        else
            activity?.runOnUiThread(mediaStopRunnable)
    }

    fun reDownloadFile() {
        var downloadList = downloadErrorList.distinct().toMutableList()
        downloadErrorList.clear()
        downloadList.forEach {
            val file = File( Base.sdcardDownLoad.path,it)
            if(!file.exists()){
                var result = HttpService().sendGetFile(Base.mediaDownloadApi + it, file)

                if (result != 200) downloadErrorList.add(it)
            }
        }
    }

    fun onDestory() {
        activity = null
        timer = null
    }
}