package com.example.kotlinsampleapplication.MediaDomain

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.VideoView

class MediaStopRunnable: Runnable {

    var tag: String = "MediaStopRunnable"
    var video: VideoView? = null
    var img: ImageView? = null
    var defaultDrawable: Drawable? = null

    fun setVideoControl(video: VideoView, img: ImageView, defaultDrawable : Drawable) {
        this.video = video
        this.img = img
        this.defaultDrawable = defaultDrawable
    }

    override fun run() {
        video?.setVisibility(View.INVISIBLE);
        img?.setVisibility(View.VISIBLE);

        try {
            if(video?.isPlaying() == true) video?.stopPlayback()

            img?.setImageDrawable(defaultDrawable)

        } catch (ex: Exception) {
            Log.i(tag, ex.message.toString())
        }
    }
}