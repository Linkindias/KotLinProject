package com.example.kotlinsampleapplication.MediaDomain

import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.VideoView
import com.example.kotlinsampleapplication.ViewModel.VideoDetial
import java.io.File
import java.io.FileInputStream
import java.util.*


class MediaPlayRunnable: Runnable {
    var tag: String = "MediaPlayRunnable"
    var video: VideoView? = null
    var img: ImageView? = null
    var sound: MediaPlayer? = null

    var path: String = ""
    var type: String = ""

    fun setVideoControl(video: VideoView, img: ImageView, sound: MediaPlayer) {
        this.video = video
        this.img = img
        this.sound = sound
    }

    fun setMediaPathType(path: String, type: String){
        this.path = path
        this.type = type
    }

    override fun run() {
        video?.setVisibility(View.VISIBLE);
        img?.setVisibility(View.VISIBLE);

        try {
            if (this.path.isNotEmpty()) {

                if (this.type == "img") {
                    var fis = FileInputStream(File(path))
                    val bmp = BitmapFactory.decodeStream(fis)
                    img?.setImageBitmap(bmp)

                    video?.setVisibility(View.INVISIBLE);
                }
                else if (this.type == "video") {
                    this.video?.setVideoPath(path)
                    this.video?.start()

                    img?.setVisibility(View.INVISIBLE);
                }
                else if (this.type == "sound") {
                    this.sound?.setVolume(100f, 100f)
                    this.sound?.setDataSource(path)
                    this.sound?.prepare();
                    this.sound?.start()
                }
            }
        } catch (ex: Exception) {
            Log.i(tag, ex.message.toString())
        }
    }
}