package com.example.kotlinsampleapplication

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinsampleapplication.ViewModel.VideoInfo
import com.google.gson.Gson


class MediaActivity : AppCompatActivity(), PlayReceiver.Receiver {
    var tag: String = "MediaActivity"
    var video: VideoView? = null
    var img: ImageView? = null
    var isStop: Boolean = false

    var currentType: String = ""
    var currentPath: String = ""
    var receiver: PlayReceiver? = null

    override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
//        tv?.setText(resultData.getString("progress").toString())

        currentPath = resultData.getString("currentPath").toString()
        currentType = resultData.getString("currentType").toString()
        video?.setVisibility(View.VISIBLE);
        img?.setVisibility(View.VISIBLE);

        if (currentPath.length > 0 && currentType == "img")
        {
            val bitmap = BitmapFactory.decodeFile(currentPath)
            img?.setImageBitmap(bitmap)
            video?.setVisibility(View.INVISIBLE);
        }
        else if (currentPath.length > 0 && currentType == "video") {
            video?.setVideoPath(currentPath)
            video?.start()
            img?.setVisibility(View.INVISIBLE);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; //轉橫向

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        receiver = PlayReceiver(Handler())
        receiver!!.setReceiver(this)

        val i = Intent(this, FileService::class.java) //open background service
        i.putExtra("receiver", receiver);
        startService(i)

        video = findViewById<View>(R.id.videoView) as VideoView
        img = findViewById<View>(R.id.imageView) as ImageView

        val play: Button = findViewById<View>(R.id.btPlay) as Button
        play.setOnClickListener(listener);

        val pause: Button = findViewById<View>(R.id.btPause) as Button
        pause.setOnClickListener(listener);

        val stop: Button = findViewById<View>(R.id.btStop) as Button
        stop.setOnClickListener(listener);

        val upload: Button = findViewById<View>(R.id.btUpload) as Button
        upload.setOnClickListener(listener);

        video?.setOnCompletionListener { mp -> //played restart
            if (currentPath.length > 0) {
                video?.setVideoPath(currentPath)
                video?.start()
            }
        }

        video?.setOnPreparedListener{ mp -> //cycle play
            mp.setLooping(true)
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
                            Log.i(tag, video.videos?.size.toString())
                        }
                        catch (ex: Exception) {
                            Log.i(tag, ex.message.toString())
                        }
                    }
                }.start()
            }
        }
    }
}