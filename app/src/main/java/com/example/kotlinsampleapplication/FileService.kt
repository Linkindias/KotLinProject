package com.example.kotlinsampleapplication

import android.app.IntentService
import android.app.SearchManager.QUERY
import android.content.Intent
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log


class FileService : IntentService("FileService") {
    var receiver: ResultReceiver? = null

    override fun onHandleIntent(intent: Intent?) {
        Log.i("FileService","start")

        receiver = intent?.getParcelableExtra("receiver") as ResultReceiver?

        val bundle = Bundle()
        bundle.putString("progress", "123")
        receiver!!.send(1, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}