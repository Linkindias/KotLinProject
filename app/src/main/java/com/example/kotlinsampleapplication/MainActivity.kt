package com.example.kotlinsampleapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.base.Common.Companion.hideBar
import com.example.kotlinsampleapplication.base.HttpApiServer
import com.example.kotlinsampleapplication.dal.media.MediaApi
import com.example.kotlinsampleapplication.fragment.BlankFragment
import com.example.kotlinsampleapplication.fragment.FullscreenFragment
import java.io.IOException


class MainActivity : AppCompatActivity() {
    val tag: String = "MainActivity"

    private val fullscreenFragment = FullscreenFragment()
    private val blackFragment = BlankFragment()

    private var httpApiServer: HttpApiServer? = null
    private var mediaApi: MediaApi? = null

    var fragments: MutableList<Fragment> = mutableListOf()

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

        hideBar(this)

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
        btSerial.setOnClickListener {
            val serialActivity = Intent(this, SerialActivity::class.java)
            startActivity(serialActivity)
        }
        val btReceiver: Button = findViewById<View>(R.id.btReceiver) as Button
        btReceiver.setOnClickListener {
            val it = Intent(notification)
            it.putExtra("loadType", "media")
            sendBroadcast(it)
        }
        val btService: Button = findViewById<View>(R.id.btService) as Button
        btService.setOnClickListener {
            val serialActivity = Intent(this, BackgroundUpdateActivity::class.java)
            startActivity(serialActivity)
        }

        val btfFragment: Button = findViewById<View>(R.id.btfirstFragment) as Button
        btfFragment.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            var fullFragment = fragments.find { it == fullscreenFragment }

            if (fullFragment == null) {
                fragments.add(fullscreenFragment)
                transaction.add(R.id.fragment_container, fullscreenFragment)
            }
            else {
                fragments.forEach {
                    transaction.hide(it)
                }
                transaction.show(fullFragment)
            }
            transaction.commit()
        }
        val btsFragment: Button = findViewById<View>(R.id.btsecondFragment) as Button
        btsFragment.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            var bFragment = fragments.find { it == blackFragment }

            if (bFragment == null) {
                fragments.add(blackFragment)
                if (fragments.size > 0)
                    transaction.replace(R.id.fragment_container, blackFragment)
                else
                    transaction.add(R.id.fragment_container, blackFragment)
            }
            else {
                fragments.forEach {
                    transaction.hide(it)
                }
                transaction.show(bFragment)
            }
            transaction.commit()
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

    private fun addFragment(f: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, f)
        transaction.commit()
    }

    private fun replaceFragment(f : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, f)
        transaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()

        if(httpApiServer != null) httpApiServer!!.stop();
        if (mediaApi != null) mediaApi = null
        if (bocastReceiver != null) unregisterReceiver(bocastReceiver)
    }
}



