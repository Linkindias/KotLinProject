package com.example.kotlinsampleapplication.Room

import android.util.Log
import androidx.room.*
import com.example.kotlinsampleapplication.Base
import com.example.kotlinsampleapplication.Base.Companion.sdf
import java.util.*

@Entity(tableName = "mediaInfo")
public class MediaEntity {

    @PrimaryKey(autoGenerate = true) //id自動累加
    var id = 0

    @ColumnInfo(name = "startDate") var startDate: Date? = null
    @ColumnInfo(name = "endDate") var endDate: Date? = null
    @ColumnInfo(name = "fileName") var fileName: String = ""
    @ColumnInfo(name = "type") var type: String = ""
    @ColumnInfo(name = "path") var path: String = ""

    constructor(fileName: String, type: String, path: String, startDate: Date?, endDate: Date?) {
        this.fileName = fileName
        this.type = type
        this.path = path
        this.startDate = startDate
        this.endDate = endDate
    }

//    @Ignore //如果要使用多形的建構子，必須加入@Ignore
//    constructor(id: Int, fileName: String, type: String, path: String) {
//        this.id = id
//        this.fileName = fileName
//        this.type = type
//        this.path = path
//    }
}
