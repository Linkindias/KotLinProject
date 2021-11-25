package com.example.kotlinsampleapplication

import fi.iki.elonen.NanoHTTPD
import java.io.FileNotFoundException
import java.io.IOException
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import fi.iki.elonen.NanoHTTPD.Response.IStatus
import java.io.File
import java.io.InputStream

class HttpApiServer(port:Int) : NanoHTTPD(port) {
    val tag: String = "HttpServer"

    override fun serve(session: IHTTPSession): Response {
        Log.i(tag , "getMethod:$session.getMethod()")

        if (Method.GET.equals(session.getMethod())) {

            var files = HashMap<String, String>();
            var header = session.getHeaders();

            try {
                session.parseBody(files);

                var body = session.getQueryParameterString();
                Log.i(tag , "header:$header")
                Log.i(tag , "body:$body")

                header.get("http-client-ip")
            } catch (e: IOException) {
                e.printStackTrace();
            } catch (e: ResponseException) {
                e.printStackTrace();
            }

            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", "HelloWorld");
        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/html", "not get");
    }

    fun handle(session: IHTTPSession): Response {
        Log.i(tag, session.uri.toString())
        try {
            var file  = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"a.mp4")
            return fi.iki.elonen.NanoHTTPD.newChunkedResponse(Response.Status.OK,"text/html",file.inputStream())
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
        return fi.iki.elonen.NanoHTTPD.newFixedLengthResponse("<!DOCTYPE html><html><body>Sorry, We Can't Find $url !</body></html>\n")
    }
}