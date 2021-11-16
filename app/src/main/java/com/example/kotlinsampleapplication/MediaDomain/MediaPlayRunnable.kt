package com.example.kotlinsampleapplication.MediaDomain

import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.VideoView
import java.io.File
import java.io.FileInputStream


class MediaPlayRunnable: Runnable {
    var tag: String = "MediaPlayRunnable"
    var video: VideoView? = null
    var img: ImageView? = null

    var path: String = ""
    var type: String = ""

    fun setVideoControl(video: VideoView, img: ImageView) {
        this.video = video
        this.img = img
    }

    fun setMediaPathType(path: String, type: String){
        this.path = path
        this.type = type
    }

    override fun run() {
        video?.setVisibility(View.VISIBLE);
        img?.setVisibility(View.VISIBLE);

        try {
            if (this.path.isNotEmpty() && this.type == "img") {
                Log.i(tag, "set img")
                var fis = FileInputStream(File(path))
                val bmp = BitmapFactory.decodeStream(fis)
                img?.setImageBitmap(bmp)

                video?.setVisibility(View.INVISIBLE);
            }
            else if (this.path.isNotEmpty() && this.type == "video") {
                Log.i(tag, "set video")
                this.video?.setVideoPath(path)
                this.video?.start()

                img?.setVisibility(View.INVISIBLE);
            }
        } catch (ex: Exception) {
            Log.i(tag, ex.message.toString())
        }
    }
}