package com.example.kotlinsampleapplication.MediaDomain

import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.VideoView

class MediaStopRunnable: Runnable {

    var tag: String = "MediaStopRunnable"
    var video: VideoView? = null
    var img: ImageView? = null

    fun setVideoControl(video: VideoView, img: ImageView) {
        this.video = video
        this.img = img
    }

    override fun run() {
        video?.setVisibility(View.INVISIBLE);
        img?.setVisibility(View.VISIBLE);

        try {
            if(video?.isPlaying() == true) video?.stopPlayback()

            val bitmap = BitmapFactory.decodeFile("") //預設圖片
            img?.setImageBitmap(bitmap)

        } catch (ex: Exception) {
            Log.i(tag, ex.message.toString())
        }
    }
}