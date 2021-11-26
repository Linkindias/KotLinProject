package com.example.kotlinsampleapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.base.Common.Companion.sdf
import com.example.kotlinsampleapplication.dal.media.MediaDBHelper
import com.example.kotlinsampleapplication.dal.media.MediaEntity
import com.example.kotlinsampleapplication.dal.media.MediaRepository

class RoomActivity : AppCompatActivity() {
    val tag: String = "RoomActivity"

    val videoRepo: MediaRepository by lazy {
        MediaRepository(MediaDBHelper.getDatabase(this)?.mediaDao()!!)
    }

    var handler:Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        val et: EditText = findViewById<View>(R.id.etVideo) as EditText

        val upDate: Button = findViewById<View>(R.id.btUpdate) as Button
        upDate.setOnClickListener {
            Thread {
                videoRepo.update(MediaEntity("test","img", "testPath" ,sdf.parse("2021-11-25 00:00:00"), sdf.parse("2021-11-25 00:00:00")))
                var videos = videoRepo.getAll()

                handler.post {
                    var sb = StringBuilder()
                    videos.forEach {
                        sb.append(it.id.toString() + " " + it.fileName + " " + it.type + " " + it.path + " " + it.startDate + " " + it.endDate + "\r\n")
                    }
                    et.setText("videoDB :\r\n" + sb.toString())
                }
            }.start()
        }

        val insert: Button = findViewById<View>(R.id.btInsert) as Button
        insert.setOnClickListener {
            Thread {
                videoRepo.insertItem(MediaEntity("test","video","123", sdf.parse("2021-01-01 00:00:00"),sdf.parse("2021-01-01 00:00:00") ))
                var videos = videoRepo.getAll()

                handler.post {
                    var sb = StringBuilder()
                    videos.forEach {
                        sb.append(it.id.toString() + it.fileName + " " + it.type + " " + it.path + " " + it.startDate + " " + it.endDate + "\r\n")
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
                var videos = videoRepo.getAll()
                var sb = StringBuilder()
                videos.forEach {
                    sb.append(it.id.toString() + it.fileName + " " + it.type + " " + it.path + " " + sdf.format(it.startDate) + " " + sdf.format(it.endDate) + "\r\n")
                }
                handler.post {
                    et.setText("videoDB :" + sb.toString())
                }
            }.start()
        }
    }
}