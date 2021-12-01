package com.example.kotlinsampleapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinsampleapplication.base.HttpApiServer
import com.example.kotlinsampleapplication.dal.media.MediaApi
import java.io.IOException


class MainActivity : AppCompatActivity() {
    val tag: String = "MainActivity"

    private var httpApiServer: HttpApiServer? = null
    private var mediaApi: MediaApi? = null

    companion object {
        val notification = "com.example.kotlinsampleapplication.webapi"
    }

    var bocastReceiver: BroadcastReceiver? = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            if(intent!!.getAction() == notification){
                val loadType = intent!!.getStringExtra("loadType")
                Log.i(tag, "onReceive:" + loadType)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT >= 23) {
            val REQUEST_CODE_CONTACT = 101
            val permissions = arrayOf<String>(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

            for (str in permissions) {
                if (checkSelfPermission(str!!) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, REQUEST_CODE_CONTACT)
                }
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(bocastReceiver, IntentFilter(notification))

        val btActivity: Button = findViewById<View>(R.id.btActivity) as Button
        btActivity.setOnClickListener {
            val testActivity = Intent(this, TestActivity::class.java)
            startActivity(testActivity)
        }
        val btVideo: Button = findViewById<View>(R.id.btVideo) as Button
        btVideo.setOnClickListener {
            val mediaActivity = Intent(this, MediaActivity::class.java)
            startActivity(mediaActivity)
        }
        val btRoomdb: Button = findViewById<View>(R.id.btRoomDb) as Button
        btRoomdb.setOnClickListener {
            val dbActivity = Intent(this, RoomActivity::class.java)
            startActivity(dbActivity)
        }
        val btSerial: Button = findViewById<View>(R.id.btSerial) as Button
        btRoomdb.setOnClickListener {
            val serialActivity = Intent(this, SerialActivity::class.java)
            startActivity(serialActivity)
        }
        val btReceiver: Button = findViewById<View>(R.id.btReceiver) as Button
        btReceiver.setOnClickListener {
            val it = Intent(notification)
            it.putExtra("loadType", "media")
            sendBroadcast(it)
        }

        try {
            mediaApi = MediaApi(this)
            httpApiServer = HttpApiServer(mediaApi!!, 8092)
            httpApiServer!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun sendBoardCast(type:String){
        val it = Intent(notification)
        it.putExtra("loadType", type)
        sendBroadcast(it)
    }

    override fun onDestroy() {
        super.onDestroy()

        if(httpApiServer != null) httpApiServer!!.stop();
        if (mediaApi != null) mediaApi = null
        if (bocastReceiver != null) unregisterReceiver(bocastReceiver)
    }
}