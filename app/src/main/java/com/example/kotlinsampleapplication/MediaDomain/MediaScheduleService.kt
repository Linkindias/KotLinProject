package com.example.kotlinsampleapplication.MediaDomain

import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.util.Log
import android.widget.ImageView
import android.widget.VideoView
import com.example.base.Common
import com.example.base.Common.Companion.sdf
import com.example.base.HttpService
import com.example.kotlinsampleapplication.MediaActivity
import com.example.kotlinsampleapplication.Service.ScheduleDownLoadService.Companion.downloadErrorList
import com.example.kotlinsampleapplication.ViewModel.VideoDetial
import java.io.File
import java.util.*

class MediaScheduleService {
    val tag: String = "MediaScheduleService"

    var activity: MediaActivity? = null
    var timer: Timer? = null

    var mediaSchedules = ArrayList<VideoDetial>()

    constructor(activity: MediaActivity) {
        this.activity = activity
    }

    fun getMediaSchedule(schedules: ArrayList<VideoDetial>) {
        mediaSchedules = schedules

        try {
            var dtNow = Date()
            var currentStartSchedule: VideoDetial? = null
            var nextStartSchedule: VideoDetial? = null

            var indexflag = -1;
            mediaSchedules.forEachIndexed { index, it ->
                if (IsCurrentSchedule(indexflag, it, dtNow)) {
                    currentStartSchedule = it
                    indexflag = index

                } else if(IsNextSchedule(indexflag, dtNow, it)) {
                    nextStartSchedule = it
                    indexflag = index
                }
            }

            if (currentStartSchedule != null ) {
                activity?.setMediaPathType(currentStartSchedule!!.path, currentStartSchedule!!.type)
                activity?.executeMediaPlay()
                findCurrentScheduleToEndDate(indexflag)

            } else if (nextStartSchedule != null ) {
                activity?.executeMediaStop()
                findNextScheduleToStartDate(indexflag)
            }
            else
                activity?.executeMediaStop()

        } catch (ex: Exception) {
            Log.e(tag, ex.message.toString())
        }
    }

    private fun IsNextSchedule(indexflag: Int, dtNow: Date, it: VideoDetial)
                = indexflag == -1 && dtNow <= it.sDate && dtNow <= it.eDate

    private fun IsCurrentSchedule(indexflag: Int, it: VideoDetial, dtNow: Date)
                = indexflag == -1 && it.sDate!! <= dtNow && dtNow <= it.eDate

    fun findCurrentScheduleToEndDate(currentIndex: Int) {
        if (mediaSchedules.size > currentIndex) {
            val endDuration: Long = mediaSchedules[currentIndex].eDate!!.time - Date().time
            Log.i(tag, "e:" + sdf.format(mediaSchedules[currentIndex].eDate) + " d:" + sdf.format(Date()))

            timer = Timer()
            timer?.schedule( object : TimerTask() {
                override fun run() {
                    activity?.executeMediaStop()
                    timer?.cancel()
                    timer?.purge()
                    timer = null

                    findNextScheduleToStartDate(currentIndex + 1)
                }
            }, endDuration)
        }
        else
            activity?.executeMediaStop()
    }

    fun findNextScheduleToStartDate(nextIndex: Int) {
        if (mediaSchedules.size > nextIndex) {
            val startDuration: Long = mediaSchedules[nextIndex].sDate!!.time - Date().time
            Log.i(tag, "s:" + sdf.format(mediaSchedules[nextIndex].sDate) + " d:" + sdf.format(Date()))

            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    activity?.setMediaPathType(mediaSchedules[nextIndex].path, mediaSchedules[nextIndex].type)
                    activity?.executeMediaPlay()
                    timer?.cancel()
                    timer?.purge()
                    timer = null

                    findCurrentScheduleToEndDate(nextIndex)
                }
            }, startDuration)
        }
        else
            activity?.executeMediaStop()
    }

    fun onDestory() {
        activity = null
        timer = null
    }
}