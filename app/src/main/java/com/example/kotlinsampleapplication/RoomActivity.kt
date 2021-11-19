package com.example.kotlinsampleapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.kotlinsampleapplication.Room.VideoDBHelper
import com.example.kotlinsampleapplication.Room.VideoDao
import com.example.kotlinsampleapplication.Room.VideoEntity
import com.example.kotlinsampleapplication.Room.VideoRepository

class RoomActivity : AppCompatActivity() {

    val videoRepo: VideoRepository by lazy {
        VideoRepository(VideoDBHelper.getDatabase(this)?.videoDao()!!)
    }

    var handler:Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        val et: EditText = findViewById<View>(R.id.etVideo) as EditText

        val upDate: Button = findViewById<View>(R.id.btUpdate) as Button
        upDate.setOnClickListener {
            Thread {
                videoRepo.update(VideoEntity("test","img", "testPath"))
                var videos = videoRepo.getAll()

                handler.post {
                    var sb = StringBuilder()
                    videos.forEach {
                        sb.append(it.id.toString() + " " + it.fileName + " " + it.type + " " + it.path + "\r\n")
                    }
                    et.setText("videoDB :\r\n" + sb.toString())
                }
            }.start()
        }

        val insert: Button = findViewById<View>(R.id.btInsert) as Button
        insert.setOnClickListener {
            Thread {
                videoRepo.insertItem(VideoEntity("test","video", "123"))
                var videos = videoRepo.getAll()

                handler.post {
                    var sb = StringBuilder()
                    videos.forEach {
                        sb.append(it.id.toString() + it.fileName + " " + it.type + " " + it.path + "\r\n")
                    }
                    et.setText("videoDB :\r\n" + sb.toString())
                }
            }.start()
        }
        val delete: Button = findViewById<View>(R.id.btDelete) as Button
        delete.setOnClickListener {
            Thread {
                videoRepo.deleteAll()
                var video = videoRepo.getAll()

                handler.post {
                    et.setText("videoDB size:" + video.size.toString())
                }
            }.start()
        }
        val query: Button = findViewById<View>(R.id.btQuery) as Button
        query.setOnClickListener {
            Thread{
                var video = videoRepo.getAll()

                handler.post {
                    et.setText("videoDB size:" + video.size.toString())
                }
            }.start()
        }
    }
}