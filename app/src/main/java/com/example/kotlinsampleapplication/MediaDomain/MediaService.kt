package com.example.kotlinsampleapplication.MediaDomain

import android.util.Log
import android.widget.ImageView
import android.widget.VideoView
import com.example.kotlinsampleapplication.ViewModel.VideoDetial
import java.util.ArrayList

class MediaService {
    var tag: String = "MediaService"

    var mediaPlayRunnable: MediaPlayRunnable = MediaPlayRunnable()
    var mediaStopRunnable: MediaStopRunnable = MediaStopRunnable()
    var videoSchedules = ArrayList<VideoDetial>()
    var executeRunnable: Runnable? = null

    constructor(video: VideoView, img: ImageView) {

        mediaPlayRunnable.setVideoControl(video!!, img!!)
        mediaStopRunnable.setVideoControl(video!!, img!!)
    }

    public fun setMediaPathType(flag :Int, schedules: ArrayList<VideoDetial>, path: String, type: String, sDate: String, eDate: String) {
        videoSchedules = schedules

        if (flag == FileService.playflag) {
            Log.i("$tag stop:", eDate)
            Log.i("$tag schedules:", schedules.size.toString())
            mediaPlayRunnable.setMediaPathType(path, type)
            executeRunnable = mediaPlayRunnable
        }
        else if (flag == FileService.stopflag) {
            Log.i("$tag start:", sDate)
            Log.i("$tag schedules:", schedules.size.toString())
            executeRunnable = mediaStopRunnable
        }
    }

    public fun runOnUiRunnable(): Runnable{
        return executeRunnable!!;
    }
}