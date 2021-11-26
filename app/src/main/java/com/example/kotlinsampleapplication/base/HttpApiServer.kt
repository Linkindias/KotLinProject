package com.example.kotlinsampleapplication.base

import fi.iki.elonen.NanoHTTPD
import android.util.Log
import com.example.base.Common
import com.example.base.Common.Companion.jsonType
import com.example.kotlinsampleapplication.Model.WeatherModel
import com.example.kotlinsampleapplication.dal.media.MediaApi
import com.google.gson.Gson
import fi.iki.elonen.NanoHTTPD.ResponseException
import java.io.File
import java.lang.Exception

class HttpApiServer(mediaApi: MediaApi, port:Int) : NanoHTTPD(port) {
    val tag: String = "HttpApiServer"
    var mediaApi: MediaApi = mediaApi

    override fun serve(session: IHTTPSession): Response {
        var exception = ""
        try {
            var apiActions = session.uri.split("/")
            if (apiActions.size > 1) {

                var result = ""

                if (Method.GET.equals(session.getMethod())) {

                    if (apiActions[1].equals("Media") && apiActions[2] != null) result = mediaApi.findActionMethod(apiActions,session.parms)

                    if (result.equals("")) return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, jsonType, "{ 'msg' : 'not Method' }");

                    return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK,Common.jsonType, result );

                } else if (Method.POST.equals(session.getMethod())) {

                    val files: Map<String, String> = HashMap()
                    session.parseBody(files)

                    if (apiActions[1].equals("Media") && apiActions[2] != null) result = mediaApi.findActionMethod(apiActions, files)

                    Log.i(tag, "r:" + result)
                    if (result.equals("")) return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, jsonType, "{ 'msg' : 'not Method' }");

                    return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK,Common.jsonType, result );

                } else if (Method.DELETE.equals(session.getMethod())) {

                }
            }
        }
        catch (e: Exception){
            exception = "{ 'exception': '${e.message}' }"
        }
        return newFixedLengthResponse(Response.Status.BAD_REQUEST, jsonType, exception);
    }
}