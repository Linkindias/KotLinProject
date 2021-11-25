package com.example.kotlinsampleapplication.dal.media

import android.content.Context
import com.google.gson.Gson




class MediaApi {
    val tag: String = "MediaApi"
    var context: Context? = null

    constructor(activity: Context ){
        context = activity
    }

    val videoRepo: MediaRepository by lazy {
        MediaRepository(MediaDBHelper.getDatabase(this.context).mediaDao()!!)
    }

    fun getMediaSchedules(): String {
        return Gson().toJson(videoRepo.getAll())
    }

    fun getMediaSchedulesByType(type:String): String {
        return Gson().toJson(videoRepo.getMediaByType(type))
    }

    fun getMediaSchedulesByPath(path:String): String {
        return Gson().toJson(videoRepo.getMediaByPath(path))
    }

    fun getMediaSchedulesByFileName(fileName:String): String {
        return Gson().toJson(videoRepo.getMediaByFileName(fileName))
    }
}