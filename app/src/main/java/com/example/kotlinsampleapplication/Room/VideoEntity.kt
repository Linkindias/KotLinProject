package com.example.kotlinsampleapplication.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "videoInfo")
public class VideoEntity {

    @PrimaryKey(autoGenerate = true) //id自動累加
    var id = 0

    @ColumnInfo(name = "fileName") var fileName: String = ""
    @ColumnInfo(name = "type") var type: String = ""
    @ColumnInfo(name = "path") var path: String = ""


    constructor(fileName: String, type: String, path: String) {
        this.fileName = fileName
        this.type = type
        this.path = path
    }

//    @Ignore //如果要使用多形的建構子，必須加入@Ignore
//    constructor(id: Int, fileName: String, type: String, path: String) {
//        this.id = id
//        this.fileName = fileName
//        this.type = type
//        this.path = path
//    }
}
