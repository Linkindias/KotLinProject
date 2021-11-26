package com.example.kotlinsampleapplication.dal.media

import android.content.Context
import android.util.Log
import com.example.base.Common.Companion.sdf
import com.example.kotlinsampleapplication.Model.MediaCModel
import com.example.kotlinsampleapplication.Model.MediaCUModel
import com.example.kotlinsampleapplication.Model.MediaDModel
import com.google.gson.Gson
import java.util.*


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
            url[2].equals("insertMedia") -> {
                var medias = Gson().fromJson(para.get("postData").toString(), MediaCModel::class.java)
                return insertMediaSchedules(medias.info)
            }
            url[2].equals("updateMedia") -> {
                var media = Gson().fromJson(para.get("postData").toString(), MediaCUModel::class.java)
                return updateMediaScheduleByPara(media.path,media.type,media.fileName,sdf.parse(media.startDate), sdf.parse(media.endDate))
            }
            url[2].equals("deleteMedia") -> {
                var media = Gson().fromJson(para.get("postData").toString(), MediaDModel::class.java)
                return deleteMediaScheduleByFileName(media.fileName)
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

    private fun insertMediaSchedules(medias: Array<MediaCUModel>): String {
        var mediaEntitys:MutableList<MediaEntity> = mutableListOf()
        medias.forEach {
            mediaEntitys.add(MediaEntity(it.fileName,it.type, it.path, sdf.parse(it.startDate),sdf.parse(it.endDate)))
        }
        return Gson().toJson(videoRepo.insertMediaSchedule(mediaEntitys))
    }

    private fun updateMediaScheduleByPara(path: String, type: String, fileName: String, startDate: Date, endDate: Date): String {
        return Gson().toJson(videoRepo.updateMediaSchedule(path, type, fileName, startDate, endDate))
    }

    private fun deleteMediaScheduleByFileName(fileName: String): String {
        return Gson().toJson(videoRepo.deleteMediaSchedule(fileName))
    }
}