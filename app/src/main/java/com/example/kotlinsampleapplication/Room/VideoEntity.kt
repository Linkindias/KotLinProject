package com.example.kotlinsampleapplication.Room

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = VideoEntity.TABLE_NAME)
public class VideoEntity {

    companion object {
        const val TABLE_NAME = "videoInfo"
    }

    @PrimaryKey(autoGenerate = true) //id自動累加
    var id = 0
    var fileName: String = ""
    var type: String = ""
    var path: String = ""
    var sDate: Date? = null
    var eDate: Date? = null

    constructor(fileName: String, type: String, path: String, sDate: Date, eDate: Date) {
        this.fileName = fileName
        this.type = type
        this.path = path
        this.sDate = sDate
        this.eDate = eDate
    }

    @Ignore //如果要使用多形的建構子，必須加入@Ignore
    constructor(id: Int, fileName: String, type: String, path: String, sDate: Date, eDate: Date) {
        this.id = id
        this.fileName = fileName
        this.type = type
        this.path = path
        this.sDate = sDate
        this.eDate = eDate
    }
}
