package com.example.kotlinsampleapplication.MediaDomain

import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.VideoView
import com.example.kotlinsampleapplication.Base
import com.example.kotlinsampleapplication.Base.Companion.sdf
import com.example.kotlinsampleapplication.Base.Companion.sdfJson
import com.example.kotlinsampleapplication.MediaActivity
import com.example.kotlinsampleapplication.ViewModel.VideoDetial
import java.util.*

class MediaScheduleService {
    val tag: String = "MediaScheduleService"

    var activity: MediaActivity? = null
    var mediaPlayRunnable: MediaPlayRunnable = MediaPlayRunnable()
    var mediaStopRunnable: MediaStopRunnable = MediaStopRunnable()
    var timer: Timer? = null

    var videoSchedules = ArrayList<VideoDetial>()

    constructor(activity: MediaActivity, video: VideoView, img: ImageView, sound: MediaPlayer, defaultDrawable: Drawable?) {
        this.activity = activity
        mediaPlayRunnable.setVideoControl(video!!, img!!, sound!!)
        mediaStopRunnable.setVideoControl(video!!, img!!, sound!!, defaultDrawable!!)
    }

    fun getVideoSchedule(schedules: ArrayList<VideoDetial>) {
        videoSchedules = schedules

        try {
            var dtNow = Date()
            var currentStartSchedule: VideoDetial? = null
            var nextStartSchedule: VideoDetial? = null

            var indexflag = -1;
            videoSchedules.forEachIndexed { index, it ->
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
        } catch (ex: Exception) {
            Log.e(tag, ex.message.toString())
        }
    }

    fun findCurrentScheduleToEndDate(currentIndex: Int) {
        if (videoSchedules.size > currentIndex) {
            val endDuration: Long = videoSchedules[currentIndex].eDate!!.time - Date().time
            Log.i(tag, "e:" + sdf.format(videoSchedules[currentIndex].eDate) + " d:" + sdf.format(Date()))

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
    }

    fun findNextScheduleToStartDate(nextIndex: Int) {
        if (videoSchedules.size > nextIndex) {
            val startDuration: Long = videoSchedules[nextIndex].sDate!!.time - Date().time
            Log.i(tag, "s:" + sdf.format(videoSchedules[nextIndex].sDate) + " d:" + sdf.format(Date()))

            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    mediaPlayRunnable.setMediaPathType(videoSchedules[nextIndex].path, videoSchedules[nextIndex].type)
                    activity?.runOnUiThread(mediaPlayRunnable)
                    timer?.cancel()
                    timer?.purge()
                    timer = null

                    findCurrentScheduleToEndDate(nextIndex)
                }
            }, startDuration)
        }
    }

    fun onDestory() {
        activity = null
        timer = null
    }
}