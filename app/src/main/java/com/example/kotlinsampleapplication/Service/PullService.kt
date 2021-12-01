package com.example.kotlinsampleapplication.Service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class PullService : Service() {
    private val tag: String = "PullService"
    private val binder = LocalBinder()

    override fun onCreate() {
        super.onCreate()
        Log.i(tag, "PullService start")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): PullService = this@PullService
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(tag, "PullService stop")
    }
}