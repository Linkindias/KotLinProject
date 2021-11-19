package com.example.kotlinsampleapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinsampleapplication.Room.VideoDBHelper
import com.example.kotlinsampleapplication.Room.VideoDao
import com.example.kotlinsampleapplication.Room.VideoRepository

class RoomActivity : AppCompatActivity() {

    val videoRepo: VideoRepository by lazy {
        VideoRepository(VideoDBHelper.getDatabase(this)?.videoDao()!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

    }
}