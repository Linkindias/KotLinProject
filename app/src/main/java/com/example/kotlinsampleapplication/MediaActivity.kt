package com.example.kotlinsampleapplication

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinsampleapplication.MediaDomain.FileService
import com.example.kotlinsampleapplication.MediaDomain.MediaPlayRunnable
import com.example.kotlinsampleapplication.MediaDomain.MediaService
import com.example.kotlinsampleapplication.MediaDomain.MediaStopRunnable
import com.example.kotlinsampleapplication.ViewModel.VideoDetial
import java.util.ArrayList


class MediaActivity : AppCompatActivity()  {
    var tag: String = "MediaActivity"
    var video: VideoView? = null
    var img: ImageView? = null

    var currentPath: String = ""

    var isStop: Boolean = false
    var serviceIntent: Intent? = null

    var mediaService: MediaService? = null

    val receiver: ResultReceiver = object : ResultReceiver(Handler()) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {

            mediaService?.setMediaPathType(resultCode,
                                            resultData.getParcelableArrayList<VideoDetial>("schedules") as ArrayList<VideoDetial>,
                                            resultData.getString("currentPath").toString(),
                                            resultData.getString("currentType").toString(),
                                            resultData.getString("startDate").toString(),
                                            resultData.getString("stopDate").toString())
            runOnUiThread(mediaService?.runOnUiRunnable());
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; //轉橫向

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        video = findViewById<View>(R.id.videoView1) as VideoView
        img = findViewById<View>(R.id.imageView1) as ImageView
        mediaService = MediaService(video!!, img!!)

        val play: Button = findViewById<View>(R.id.btPlay) as Button
        play.setOnClickListener(listener);

        val pause: Button = findViewById<View>(R.id.btPause) as Button
        pause.setOnClickListener(listener);

        val stop: Button = findViewById<View>(R.id.btStop) as Button
        stop.setOnClickListener(listener);

        video?.setOnCompletionListener { mp -> //played restart

            if (currentPath.isNotEmpty()) {
                video?.setVideoPath(currentPath)
                video?.start()
            }
        }

        video?.setOnPreparedListener{ mp -> //cycle play
            mp.setLooping(true)
        }

        serviceIntent = Intent(this, FileService::class.java) //open background service
        serviceIntent!!.putExtra("receiver", receiver);
        startService(serviceIntent)
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
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(serviceIntent);
    }
}