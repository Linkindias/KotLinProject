package com.example.kotlinsampleapplication.base

import fi.iki.elonen.NanoHTTPD
import java.io.FileNotFoundException
import java.io.IOException
import android.os.Environment
import android.util.Log
import com.example.base.Common
import com.example.base.Common.Companion.jsonType
import com.example.kotlinsampleapplication.dal.media.MediaApi
import java.io.File
import java.lang.Exception

class HttpApiServer(mediaApi: MediaApi, port:Int) : NanoHTTPD(port) {
    val tag: String = "HttpServer"
    var mediaApi: MediaApi = mediaApi

    override fun serve(session: IHTTPSession): Response {
        var exception = ""
        try {
            var apiActions = session.uri.split("/")
            if (apiActions.size > 1) {

                if (Method.GET.equals(session.getMethod())) {

                    var result = ""

                    if (apiActions[1].equals("Media") && apiActions[2] != null) result = mediaApi.findActionMethod(apiActions,session.parms)

                    if (result.equals("")) return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, jsonType, "{ 'msg' : 'not Method' }");

                    return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK,Common.jsonType, result );

                } else if (Method.POST.equals(session.getMethod())) {

                } else if (Method.PUT.equals(session.getMethod())) {

                } else if (Method.DELETE.equals(session.getMethod())) {

                }
            }
        }
        catch (e: Exception){
            exception = "{ 'exception': '${e.message}' }"
        }
        return newFixedLengthResponse(Response.Status.BAD_REQUEST, jsonType, exception);
    }

    fun handle(session: IHTTPSession): Response {
        Log.i(tag, session.uri.toString())
        try {
            var file  = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"a.mp4")
            return newChunkedResponse(Response.Status.OK,"text/html",file.inputStream())
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ResponseException) {
            e.printStackTrace()
        }
        return response404(session, session.uri)
    }

    private fun response404( session: IHTTPSession?, url: String? ): Response {
        Log.i(tag, "response404" + session!!.uri.toString())
        return newFixedLengthResponse("<!DOCTYPE html><html><body>Sorry, We Can't Find $url !</body></html>\n")
    }
}