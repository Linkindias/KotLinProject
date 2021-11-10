package com.example.kotlinsampleapplication

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinsampleapplication.ViewModel.VideoInfo
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception


class VideoActivity : AppCompatActivity() {
    var tag: String = "VideoActivity"
    var video: VideoView? = null
    var isStop: Boolean = false
    var mutableList: MutableList<String>  = mutableListOf()
    var currentPath: String = ""
    var hs: HttpService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; //轉橫向

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        hs = HttpService()

        var file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"")
        Log.i(tag,file.path)
        file.listFiles()
            .forEach {
                if(it.name.endsWith(".mp4"))
                    mutableList.add(it.path)
            }

        video = findViewById<View>(R.id.videoView1) as VideoView
        currentPath = mutableList[0]
        video?.setVideoPath(currentPath)
        video?.start()

        val play: Button = findViewById<View>(R.id.btPlay) as Button
        play.setOnClickListener(listener);

        val pause: Button = findViewById<View>(R.id.btPause) as Button
        pause.setOnClickListener(listener);

        val stop: Button = findViewById<View>(R.id.btStop) as Button
        stop.setOnClickListener(listener);

        val upload: Button = findViewById<View>(R.id.btUpload) as Button
        upload.setOnClickListener(listener);

        video?.setOnCompletionListener { mp ->
            var index = 0
            if (mutableList.size > mutableList.indexOf(currentPath) + 1)
                index = mutableList.indexOf(currentPath) + 1
            else
                index = 0
            currentPath = mutableList[index]
            video?.setVideoPath(currentPath)
            video?.start()
        }
    }

    val listener = View.OnClickListener { view ->

        when (view.getId()) {
            R.id.btPlay -> {
                if (isStop) video?.setVideoPath(currentPath)
                video?.start()
            }
            R.id.btPause -> {
                video?.pause()
            }
            R.id.btStop -> {
                video?.stopPlayback()
                isStop = true
            }
            R.id.btUpload -> {
                Thread {
                    var result = hs?.sendGet("http://10.168.18.61/webapplication/api/video")
                    if (result != null) {
                        try {
                            var gson = Gson()
                            var video = gson.fromJson(result.toString(), VideoInfo::class.java)

                            if (video != null) {

                                video.videos?.forEach {
                                    Log.i(tag, it.fileName)

                                    var file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), it.fileName)
                                    Log.i(tag + "createfile:" ,file.path)
                                    val fos = FileOutputStream(file)
                                    fos.write(it.file.toByteArray())
                                    fos.close()

                                    mutableList.add(file.path)
                                }
                            }
                        }
                        catch (ex: Exception) {
                            Log.i(tag, ex.message.toString())
                        }
                        Log.i(tag, mutableList.size.toString())
                    }
                }.start()
            }
        }
    }
}