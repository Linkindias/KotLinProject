package com.example.kotlinsampleapplication

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.MediaController
import android.widget.VideoView

class StreamActivity : AppCompatActivity()  {

    val url = "https://mvvideo5.meitudata.com/571090934cea5517.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream)

       var videoView = findViewById<View>(R.id.videoView) as VideoView

        val mc = MediaController(this)
        videoView.setMediaController(mc)

        videoView.setVideoURI(Uri.parse("rtsp://tv.hindiworldtv.com:1935/live/getpnj"))
        videoView.requestFocus()
    }


}