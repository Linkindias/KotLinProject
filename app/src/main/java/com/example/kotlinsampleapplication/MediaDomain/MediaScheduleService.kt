package com.example.kotlinsampleapplication.MediaDomain

import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.VideoView
import com.example.kotlinsampleapplication.Base
import com.example.kotlinsampleapplication.Base.Companion.sdfJson
import com.example.kotlinsampleapplication.ViewModel.VideoDetial
import java.util.*

class MediaScheduleService {
    var tag: String = "MediaScheduleService"

    var mediaPlayRunnable: MediaPlayRunnable = MediaPlayRunnable()
    var mediaStopRunnable: MediaStopRunnable = MediaStopRunnable()
    var executeRunnable: Runnable? = null
    var handler: Handler = Handler()
    var timer: Timer = Timer()

    var videoSchedules = ArrayList<VideoDetial>()
    var path:String = ""
    var type:String = ""

    constructor(video: VideoView, img: ImageView, defaultDrawable: Drawable?) {
        mediaPlayRunnable.setVideoControl(video!!, img!!)
        mediaStopRunnable.setVideoControl(video!!, img!!, defaultDrawable!!)
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
        Log.i(tag, "endDuration:$endDuration")

        timer.schedule( object : TimerTask() {
            override fun run() {
                Log.i(tag, "endTimerTask call")
                handler.post {
                    findNextScheduleToStartDate()
                }
            }
        }, endDuration)
    }

    private fun findMediaPlayTime(sDate: String) {
        val startDuration: Long = sdfJson.parse(sDate).time!! - Date().time
        Log.i(tag, "startDuration:$startDuration")

        timer.schedule(object : TimerTask() {
            override fun run() {
                Log.i(tag, "startTimerTask call")
                handler.post {
                    findCurrentScheduleToEndDate()
                }
            }
        }, startDuration)
    }

    private fun findNextScheduleToStartDate() {
        mediaStopRunnable.run()
        timer.cancel()
        timer.purge()

        var nextIndex:Int = -1

        try {
            videoSchedules.forEachIndexed { index, it ->
                if(it.path == path && (videoSchedules.size - 1) > index) nextIndex = index + 1
            }
            Log.i(tag, "nextIndex:$nextIndex")
            if (nextIndex > -1)
            {
                path = videoSchedules[nextIndex].path
                type = videoSchedules[nextIndex].type
                val startDuration: Long = sdfJson.parse(videoSchedules[nextIndex].startDate).time!! - Date().time
                Log.i(tag, "startDuration:$startDuration")

                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        Log.i(tag, "startTimerTask call")
                        handler.post {
                            findCurrentScheduleToEndDate()
                        }
                    }
                }, startDuration)
            }
        } catch (ex: Exception) {
            Log.e(tag, ex.message.toString())
        }
    }

    private fun findCurrentScheduleToEndDate() {
        mediaPlayRunnable.setMediaPathType(path, type)
        mediaPlayRunnable.run()
        timer.cancel()
        timer.purge()

        var currendSchedule: VideoDetial? = null
        try {
            videoSchedules.forEach {
                if(it.path == path) currendSchedule = it
            }
            Log.i(tag, "currendSchedule:$currendSchedule")
            if (currendSchedule != null)
            {
                val endDuration: Long = sdfJson.parse(currendSchedule!!.endDate).time!! - Date().time
                Log.i(tag, "endDuration:$endDuration")

                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        Log.i(tag, "endTimerTask call")
                        handler.post {
                            findNextScheduleToStartDate()
                        }
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