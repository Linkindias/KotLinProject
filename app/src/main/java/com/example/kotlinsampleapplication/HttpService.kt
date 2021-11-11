package com.example.kotlinsampleapplication

import android.util.Log
import java.io.DataOutputStream
import java.io.FileInputStream
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
            Log.i(tag, "status:$status")

            if (status == 200){
//                result.append(urlConnection.inputStream.bufferedReader().readText())
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
            Log.d(tag, result.toString())
        }
        finally {
            urlConnection.disconnect();
        }
        Log.i(tag, result.length.toString())
        Log.i(tag, result.toString())
        return result
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

            if (status == 200)
                result = urlConnection.inputStream.bufferedReader().readText()
            else
                result = status.toString()
        }
        catch (ex: Exception) {
            result = ex.message.toString()
            Log.d(tag, result)
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

            if (status == 200)
                result = urlConnection.inputStream.bufferedReader().readText()
            else
                result = status.toString()
        }
        catch (ex: Exception) {
            result = ex.message.toString()
            Log.d(tag, result)
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

            if (status == 200)
                result = urlConnection.inputStream.bufferedReader().readText()
            else
                result = status.toString()
        }
        catch (ex: Exception) {
            result = ex.message.toString()
            Log.d(tag, result)
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