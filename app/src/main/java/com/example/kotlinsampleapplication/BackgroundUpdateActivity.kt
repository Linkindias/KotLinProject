package com.example.kotlinsampleapplication

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.example.kotlinsampleapplication.Service.PullService
import com.example.kotlinsampleapplication.Service.PullService.LocalBinder
import android.content.Intent
import com.example.base.Common.Companion.hideBar

class BackgroundUpdateActivity : AppCompatActivity() {

    private lateinit var mService: PullService
    private var mBound: Boolean = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as LocalBinder
            mService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_background_update)

        hideBar(this)

        val serviceIntent = Intent(this, PullService::class.java)
        this.bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
    }
}