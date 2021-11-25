package com.example.base

import android.util.Log
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class HttpService {
    var tag: String = "HttpService"

    fun sendGet(url: String): StringBuilder {
        var result: StringBuilder = java.lang.StringBuilder()
        var urlConnection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
        try {
            urlConnection.requestMethod = "GET";
            urlConnection.useCaches = false;
            urlConnection.allowUserInteraction = false;
            urlConnection.connectTimeout = 60000;
            urlConnection.readTimeout = 60000;
            urlConnection.connect()

            val status: Int = urlConnection.responseCode
//            Log.i(tag + "sendGet", "status:$status")

            if (status == 200){
                urlConnection.inputStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        result.append(line);
                    }
                }
            }
            else
                result.append(status.toString())
        }
        catch (ex: Exception) {
            result.append(ex.message.toString())
            Log.e(tag + "sendGet", result.toString())
        }
        finally {
            urlConnection.disconnect();
        }
        return result
    }

    fun sendGetFile(url: String, file: File):Int {
        var status: Int = 0
        var urlConnection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection

        try {
            urlConnection.requestMethod = "GET";
            urlConnection.useCaches = false;
            urlConnection.allowUserInteraction = false;
            urlConnection.connectTimeout = 60000;
            urlConnection.readTimeout = 60000;
            urlConnection.connect()

            status = urlConnection.responseCode
//            Log.i(tag + "sendGetFile", "status:$status")

            if (status == 200) {

                var fos = FileOutputStream(file);
                val inputStream = urlConnection.getInputStream();
                var bytesRead = 0;
                val buffer = ByteArray(1024)
                while (inputStream.read(buffer).apply { bytesRead = this } > 0) {
                    fos.write(buffer, 0, bytesRead);
                }
                fos.close();
                inputStream.close();
            }
        }
        catch (ex: Exception) {
            Log.e(tag + "sendGetFile", ex.message.toString())
        }
        finally {
            urlConnection.disconnect();
        }
        return status
    }

    fun sendPostJson(url: String, variable: String): String {
        var result: String = ""
        var urlConnection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
        var outputStream: DataOutputStream? = null
        try {
            urlConnection.requestMethod = "POST"
            urlConnection.doInput = true
            urlConnection.doOutput = true
            urlConnection.setRequestProperty("Content-Type", "application/json")

            outputStream = DataOutputStream(urlConnection.outputStream)
            outputStream.write(variable.toByteArray(charset("UTF-8")))

            val status: Int = urlConnection.responseCode
//            Log.i(tag + "sendPostJson", "status:$status")

            if (status == 200)
                result = urlConnection.inputStream.bufferedReader().readText()
            else
                result = status.toString()
        }
        catch (ex: Exception) {
            result = ex.message.toString()
            Log.e(tag + "sendPostJson", result)
        }
        finally {
            urlConnection.disconnect();
            outputStream?.flush()
            outputStream?.close()
        }
        return result
    }

    fun sendPostForm(url: String, variable: String): String {
        var result: String = ""

        var urlConnection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
        var outputStream: DataOutputStream? = null
        try {
            urlConnection.requestMethod = "POST"
            urlConnection.doInput = true
            urlConnection.doOutput = true
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

            outputStream = DataOutputStream(urlConnection.outputStream)
            outputStream.write(variable.toByteArray(charset("UTF-8")))

            val status: Int = urlConnection.responseCode
//            Log.i(tag + "sendPostForm", "status:$status")

            if (status == 200)
                result = urlConnection.inputStream.bufferedReader().readText()
            else
                result = status.toString()
        }
        catch (ex: Exception) {
            result = ex.message.toString()
            Log.e(tag + "sendPostJson", result)
        }
        finally {
            urlConnection.disconnect();
            outputStream?.flush()
            outputStream?.close()
        }
        return result
    }

    fun sendPostMultiPart(url: String, fis: FileInputStream, fileName: String, variables: Map<String, String>): String {
        var result: String = ""
        val twoHyphens = "--"
        val boundary = "*****" + java.lang.Long.toString(System.currentTimeMillis()) + "*****"
        val lineEnd = "\r\n"

        var urlConnection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
        var outputStream: DataOutputStream? = null
        try {
            urlConnection.doInput = true
            urlConnection.doOutput = true
            urlConnection.useCaches = false
            urlConnection.requestMethod = "POST"
            urlConnection.setRequestProperty("Connection", "Keep-Alive")
            urlConnection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0")
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")

            outputStream = DataOutputStream(urlConnection.outputStream)
            outputStream.writeBytes(twoHyphens + boundary + lineEnd)
            outputStream.writeBytes("Content-Disposition: form-data; name=\"files\"; filename=\"$fileName\"$lineEnd")
            outputStream.writeBytes("Content-Type: image/jpeg$lineEnd")
            outputStream.writeBytes("Content-Transfer-Encoding: binary$lineEnd")
            outputStream.writeBytes(lineEnd)

            val maxBufferSize = 1 * 1024 * 1024
            var bytesAvailable = fis.available()
            var bufferSize = Math.min(bytesAvailable, maxBufferSize)
            var buffer = ByteArray(bufferSize)
            var bytesRead = fis.read(buffer, 0, bufferSize)
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize)
                bytesAvailable = fis.available()
                bufferSize = Math.min(bytesAvailable, maxBufferSize)
                bytesRead = fis.read(buffer, 0, bufferSize)
            }
            outputStream.writeBytes(lineEnd)

            // Upload POST Data
            for ((key, value) in variables) {
                outputStream.writeBytes(twoHyphens + boundary + lineEnd)
                outputStream.writeBytes("Content-Disposition: form-data; name=\"$key\"$lineEnd")
                outputStream.writeBytes("Content-Type: text/plain$lineEnd")
                outputStream.writeBytes(lineEnd)
                outputStream.writeBytes(value)
                outputStream.writeBytes(lineEnd)
            }
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)
            urlConnection.connect()

            val status: Int = urlConnection.responseCode
//            Log.i(tag + "sendPostMultiPart", "status:$status")

            if (status == 200)
                result = urlConnection.inputStream.bufferedReader().readText()
            else
                result = status.toString()
        }
        catch (ex: Exception) {
            result = ex.message.toString()
            Log.e(tag + "sendPostMultiPart", result)
        }
        finally {
            urlConnection.disconnect();
            fis.close()
            outputStream?.flush()
            outputStream?.close()
        }
        return result
    }
}