package com.example.kotlinsampleapplication

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.base.Common.Companion.hideBar
import com.example.kotlinsampleapplication.MainActivity.Companion.localNotify
import com.example.kotlinsampleapplication.MediaDomain.MediaScheduleService
import com.example.kotlinsampleapplication.Service.ScheduleDownLoadService
import com.example.kotlinsampleapplication.ViewModel.VideoDetial
import java.io.File
import java.io.FileInputStream
import java.util.*
import javax.inject.Inject


class MediaActivity : AppCompatActivity()  {
    val tag: String = "MediaActivity"

    var video: VideoView? = null
    var img: ImageView? = null
    var sound: MediaPlayer? = null

    var serviceIntent: Intent? = null

    var mediaService: MediaScheduleService? = null

    var path: String = ""
    var type: String = ""
    var defaultDrawable: Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; //轉橫向

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        hideBar(this)

        defaultDrawable = ContextCompat.getDrawable(this, R.drawable.netcore)

        try {
            video = findViewById<View>(R.id.videoView1) as VideoView
            img = findViewById<View>(R.id.imageView1) as ImageView
            sound = MediaPlayer();

            mediaService = MediaScheduleService(this)

            video?.setOnPreparedListener { video -> //cycle play
                video.isLooping = true
                video.start()
            }

            video?.setOnTouchListener { v, event ->  //not touch
                true
            }

            video?.setOnCompletionListener { video -> //played restart
                video.start()
            }

            sound?.setOnPreparedListener { sound ->
                sound.isLooping = true;
                sound.setVolume(100f, 100f) //muted
                sound.start()
            }

            sound?.setOnCompletionListener { sound ->
                sound.start()
            }

            serviceIntent = Intent(this, ScheduleDownLoadService::class.java) //open background service
            serviceIntent!!.putExtra("receiver", receiver);
            startService(serviceIntent)

        } catch (e: Exception) {
            Log.i(tag, e.message.toString())
        }

        var i = Intent(localNotify)
        i.putExtra("key","this is an event")
        LocalBroadcastManager.getInstance(this).sendBroadcast(i)
    }

    var mediaPlayRunnable: Runnable = object: Runnable {
        override fun run() {
            video?.setVisibility(View.VISIBLE);
            img?.setVisibility(View.VISIBLE);

            try {
                if (path.isNotEmpty() && File(path).exists()) {

                    if (type == "img") {
                        var fis = FileInputStream(File(path))
                        val bmp = BitmapFactory.decodeStream(fis)
                        img?.setImageBitmap(bmp)

                        video?.setVisibility(View.INVISIBLE);
                    }
                    else if (type == "video") {
                        video?.setVideoPath(path)
                        video?.start()

                        img?.setVisibility(View.INVISIBLE);
                    }
                    else if (type == "sound") {
                        sound?.setVolume(100f, 100f)
                        sound?.setDataSource(path)
                        sound?.prepare();
                        sound?.start()
                    }
                }
                else {
                    //show not file
                }
            } catch (ex: Exception) {
                Log.i(tag, ex.message.toString())
            }
        }
    }

    var mediaStopRunnable: Runnable = object: Runnable {
        override fun run() {
            video?.setVisibility(View.INVISIBLE);
            img?.setVisibility(View.VISIBLE);

            try {
                if(video?.isPlaying() == true) video?.stopPlayback()
                if(sound?.isPlaying() == true) sound?.stop()

                img?.setImageDrawable(defaultDrawable)

            } catch (ex: Exception) {
                Log.i(tag, ex.message.toString())
            }
        }
    }

    val receiver: ResultReceiver = object : ResultReceiver(Handler()) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {

            if (resultCode == ScheduleDownLoadService.downLoadflag) {
                mediaService?.getMediaSchedule(resultData.getParcelableArrayList<VideoDetial>("schedules") as ArrayList<VideoDetial>)
            }
        }
    }

    fun setMediaPathType(path: String,type: String){
        this.path = path
        this.type = type
    }

    fun executeMediaPlay() {
        this.runOnUiThread(mediaPlayRunnable)
    }

    fun executeMediaStop() {
        this.runOnUiThread(mediaStopRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(serviceIntent);
        mediaService?.onDestory()
    }
}