package com.example.kotlinsampleapplication.dal.media

import android.content.Context
import com.example.base.Common
import com.google.gson.Gson
import fi.iki.elonen.NanoHTTPD


class MediaApi {
    val tag: String = "MediaApi"
    var context: Context? = null

    constructor(activity: Context ){
        context = activity
    }

    val videoRepo: MediaRepository by lazy {
        MediaRepository(MediaDBHelper.getDatabase(this.context).mediaDao()!!)
    }

    fun findActionMethod(url:List<String>,para:Map<String, String>): String{

        when {
            url[2].equals("getAll") -> {
                return getMediaSchedules()

            }
            url[2].equals("getType") -> {
                return getMediaSchedulesByType(para["type"]!!)

            }
            url[2].equals("getPath") -> {
                return getMediaSchedulesByPath(para["path"]!!)

            }
            url[2].equals("getFileName") -> {
                return getMediaSchedulesByFileName(para["fileName"]!!)
            }
            else -> return ""
        }
    }

    private fun getMediaSchedules(): String {
        return Gson().toJson(videoRepo.getAll())
    }

    private fun getMediaSchedulesByType(type:String): String {
        return Gson().toJson(videoRepo.getMediaByType(type))
    }

    private fun getMediaSchedulesByPath(path:String): String {
        return Gson().toJson(videoRepo.getMediaByPath(path))
    }

    private fun getMediaSchedulesByFileName(fileName:String): String {
        return Gson().toJson(videoRepo.getMediaByFileName(fileName))
    }
}