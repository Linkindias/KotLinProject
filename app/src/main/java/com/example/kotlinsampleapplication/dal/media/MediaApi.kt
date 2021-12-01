package com.example.kotlinsampleapplication.dal.media

import android.content.Context
import android.util.Log
import com.example.base.Common
import com.example.base.Common.Companion.sdf
import com.example.base.Common.Companion.successFlag
import com.example.kotlinsampleapplication.MainActivity
import com.example.kotlinsampleapplication.Model.MediaCModel
import com.example.kotlinsampleapplication.Model.MediaCUModel
import com.example.kotlinsampleapplication.Model.MediaDModel
import com.google.gson.Gson
import java.util.*


class MediaApi {
    val tag: String = "MediaApi"
    var mainActivity: MainActivity? = null

    constructor(activity: MainActivity ){
        mainActivity = activity
    }

    val videoRepo: MediaRepository by lazy {
        MediaRepository(MediaDBHelper.getDatabase(this.mainActivity).mediaDao()!!)
    }

    fun findActionMethod(url:List<String>,para:Map<String, String>): String{

        when {
            url[2] == "setMediaLoad" -> {
                mainActivity!!.sendBoardCast("Media")
                return Gson().toJson(successFlag)
            }
            url[2].equals("getAll") -> {
                return Gson().toJson(videoRepo.getAll())
            }
            url[2].equals("getType") -> {
                return Gson().toJson(videoRepo.getMediaByType(para["type"]!!))
            }
            url[2].equals("getPath") -> {
                return Gson().toJson(videoRepo.getMediaByPath(para["path"]!!))
            }
            url[2].equals("getFileName") -> {
                return Gson().toJson(videoRepo.getMediaByFileName(para["fileName"]!!))
            }
            url[2].equals("insertMedia") -> {
                var medias = Gson().fromJson(para.get("postData").toString(), MediaCModel::class.java)

                var mediaEntitys:MutableList<MediaEntity> = mutableListOf()
                medias.info.forEach {
                    mediaEntitys.add(MediaEntity(it.fileName,it.type, it.path, sdf.parse(it.startDate),sdf.parse(it.endDate)))
                }
                return Gson().toJson(videoRepo.insertMediaSchedule(mediaEntitys))
            }
            url[2].equals("updateMedia") -> {
                var media = Gson().fromJson(para.get("postData").toString(), MediaCUModel::class.java)
                return Gson().toJson(videoRepo.updateMediaSchedule(media.path,media.type,media.fileName,sdf.parse(media.startDate), sdf.parse(media.endDate)))
            }
            url[2].equals("deleteMedia") -> {
                var media = Gson().fromJson(para.get("postData").toString(), MediaDModel::class.java)
                return Gson().toJson(videoRepo.deleteMediaSchedule(media.fileName))
            }
            else -> return "{ 'msg': 'not method' }"
        }
    }
}