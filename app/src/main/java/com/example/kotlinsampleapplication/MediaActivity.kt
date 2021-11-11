package com.example.kotlinsampleapplication

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinsampleapplication.ViewModel.VideoDetial
import com.example.kotlinsampleapplication.ViewModel.VideoInfo
import com.google.gson.Gson
import kotlinx.coroutines.*


class MediaActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    var tag: String = "MediaActivity"
    var video: VideoView? = null
    var tv: TextView? = null
    var isStop: Boolean = false

    var schedulsList: MutableList<VideoDetial> = mutableListOf()
    var filePathList: MutableList<String> = mutableListOf()
    var currentPath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; //轉橫向

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        tv = findViewById<View>(R.id.textView) as TextView

        var receiver: PlayReceiver = PlayReceiver(this,Handler())
        val i = Intent(this, FileService::class.java)
        i.putExtra("receiver", receiver);
        startService(i)

//        var videoD1 = VideoDetial()
//        videoD1.fileName = "movie.mp4"
//        videoD1.type = "video"
//        videoD1.startDate = "2021-11-11 14:00:00"
//        videoD1.endDate = "2021-11-11 14:30:00"
//        schedulsList.add(videoD1)
//        var videoD2 = VideoDetial()
//        videoD2.fileName = "movie.mp4"
//        videoD2.type = "video"
//        videoD2.startDate = "2021-11-11 14:00:00"
//        videoD2.endDate = "2021-11-11 14:30:00"
//        schedulsList.add(videoD2)
//
//        var file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"")
//        file.listFiles()
//            .forEach {
//                if(it.name.endsWith(".mp4"))
//                    filePathList.add(it.path)
//            }
//
//        video = findViewById<View>(R.id.videoView1) as VideoView
//        if (filePathList.size > 0) {
//            currentPath = filePathList[0]
//            video?.setVideoPath(currentPath)
//            video?.start()
//        }

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
            if (filePathList.size > filePathList.indexOf(currentPath) + 1)
                index = filePathList.indexOf(currentPath) + 1
            else
                index = 0
            currentPath = filePathList[index]
            try {
                video?.setVideoPath(currentPath)
                video?.start()
            }
            catch (ex : Exception){
                Log.i(tag, ex.message.toString())
            }
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
                    var result = HttpService().sendGet("http://10.168.18.61/webapplication/api/video/FileSchedule")
                    if (result != null) {
                        try {
                            var gson = Gson()
                            var video = gson.fromJson(result.toString(), VideoInfo::class.java)
                        }
                        catch (ex: Exception) {
                            Log.i(tag, ex.message.toString())
                        }
                    }
                }.start()
            }
        }
    }


//    ResultReceiver mResultReceiver = new Re
}