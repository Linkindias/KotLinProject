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

    fun setMediaPlay(schedules: ArrayList<VideoDetial>, path: String, type: String, eDate: String) {
        videoSchedules = schedules
        this.path = path
        this.type = type
        mediaPlayRunnable.setMediaPathType(path, type)
        executeRunnable = mediaPlayRunnable

        findMediaStopTime(eDate)
    }

    fun setMediaStop(schedules: ArrayList<VideoDetial>, path: String, type: String, sDate: String) {
        videoSchedules = schedules
        this.path = path
        this.type = type
        executeRunnable = mediaStopRunnable

        findMediaPlayTime(sDate)
    }

    private fun findMediaStopTime(eDate: String) {
        val endDuration: Long = sdfJson.parse(eDate).time!! - Date().time

        timer = Timer()
        timer?.schedule( object : TimerTask() {
            override fun run() {
                findNextScheduleToStartDate()
            }
        }, endDuration)
    }

    private fun findMediaPlayTime(sDate: String) {
        val startDuration: Long = sdfJson.parse(sDate).time!! - Date().time

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                findCurrentScheduleToEndDate()
            }
        }, startDuration)
    }

    private fun findNextScheduleToStartDate() {
        activity?.runOnUiThread(mediaStopRunnable)
        timer?.cancel()
        timer?.purge()
        timer = null

        var nextIndex:Int = -1

        try {
            videoSchedules.forEachIndexed { index, it ->
                if(it.path == path && (videoSchedules.size - 1) > index) nextIndex = index + 1
            }

            if (nextIndex > -1)
            {
                path = videoSchedules[nextIndex].path
                type = videoSchedules[nextIndex].type
                val startDuration: Long = sdfJson.parse(videoSchedules[nextIndex].startDate).time!! - Date().time

                timer = Timer()
                timer?.schedule(object : TimerTask() {
                    override fun run() {
                        findCurrentScheduleToEndDate()
                    }
                }, startDuration)
            }
        } catch (ex: Exception) {
            Log.e(tag, ex.message.toString())
        }
    }

    private fun findCurrentScheduleToEndDate() {
        mediaPlayRunnable.setMediaPathType(path, type)
        activity?.runOnUiThread(mediaPlayRunnable)
        timer?.cancel()
        timer?.purge()
        timer = null

        var currendSchedule: VideoDetial? = null
        try {
            videoSchedules.forEach {
                if(it.path == path) currendSchedule = it
            }

            if (currendSchedule != null)
            {
                val endDuration: Long = sdfJson.parse(currendSchedule!!.endDate).time!! - Date().time

                timer = Timer()
                timer?.schedule(object : TimerTask() {
                    override fun run() {
                        findNextScheduleToStartDate()
                    }
                }, endDuration)
            }
        } catch (ex: Exception) {
            Log.e(tag, ex.message.toString())
        }
    }

    fun runOnUiRunnable(): Runnable{
        return executeRunnable!!;
    }

    fun onDestory() {
    }
}