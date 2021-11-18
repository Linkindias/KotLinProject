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
    var tag: String = "MediaScheduleService"

    var activity: MediaActivity? = null
    var mediaPlayRunnable: MediaPlayRunnable = MediaPlayRunnable()
    var mediaStopRunnable: MediaStopRunnable = MediaStopRunnable()
    var executeRunnable: Runnable? = null
    var timer: Timer? = null

    var videoSchedules = ArrayList<VideoDetial>()
    var path:String = ""
    var type:String = ""

    constructor(activity: MediaActivity, video: VideoView, img: ImageView, sound: MediaPlayer, defaultDrawable: Drawable?) {
        this.activity = activity
        mediaPlayRunnable.setVideoControl(video!!, img!!, sound!!)
        mediaStopRunnable.setVideoControl(video!!, img!!, sound!!, defaultDrawable!!)
    }

    fun setMediaPlay(schedules: ArrayList<VideoDetial>, path: String, type: String, currentIndex: Int) {
        videoSchedules = schedules
        this.path = path
        this.type = type
        mediaPlayRunnable.setMediaPathType(path, type)
        executeRunnable = mediaPlayRunnable

        findMediaStopTime(currentIndex)
    }

    fun setMediaStop(schedules: ArrayList<VideoDetial>, path: String, type: String, nextIndex: Int) {
        videoSchedules = schedules
        this.path = path
        this.type = type
        executeRunnable = mediaStopRunnable

        findMediaPlayTime(nextIndex)
    }

    private fun findMediaStopTime(currentIndex: Int) {
        val endDuration: Long = videoSchedules[currentIndex].eDate!!.time - Date().time

        if (videoSchedules.size > currentIndex) {
            timer = Timer()
            timer?.schedule( object : TimerTask() {
                override fun run() {
                    findNextScheduleToStartDate(currentIndex + 1)
                }
            }, endDuration)
        }
    }

    private fun findMediaPlayTime(nextIndex: Int) {
        val startDuration: Long = videoSchedules[nextIndex].sDate!!.time - Date().time

        if (videoSchedules.size > nextIndex) {
            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    findCurrentScheduleToEndDate(nextIndex)
                }
            }, startDuration)
        }
    }

    private fun findNextScheduleToStartDate(nextIndex: Int) {
        activity?.runOnUiThread(mediaStopRunnable)
        timer?.cancel()
        timer?.purge()
        timer = null

        if (videoSchedules.size > nextIndex) {
            var dtNow = Date()
            path = videoSchedules[nextIndex].path
            type = videoSchedules[nextIndex].type

            val startDuration: Long = sdfJson.parse(videoSchedules[nextIndex].startDate).time!! - dtNow.time

            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    findCurrentScheduleToEndDate(nextIndex)
                }
            }, startDuration)
        }
    }

    private fun findCurrentScheduleToEndDate(currentIndex: Int) {
        mediaPlayRunnable.setMediaPathType(path, type)
        activity?.runOnUiThread(mediaPlayRunnable)
        timer?.cancel()
        timer?.purge()
        timer = null

        if (videoSchedules.size > currentIndex) {
            var dtNow = Date()
            val endDuration: Long = sdfJson.parse(videoSchedules[currentIndex].endDate).time!! - dtNow.time

            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    findNextScheduleToStartDate(currentIndex + 1)
                }
            }, endDuration)
        }
    }

    fun runOnUiRunnable(): Runnable{
        return executeRunnable!!;
    }

    fun onDestory() {
    }
}