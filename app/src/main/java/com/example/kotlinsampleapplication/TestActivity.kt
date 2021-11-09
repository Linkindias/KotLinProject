package com.example.kotlinsampleapplication

import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinsampleapplication.Model.WeatherModel
import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TestActivity : AppCompatActivity() {
    var tag: String = "TestActivity"
    var hs: HttpService? = null
    var hd: Handler? = null
    var editShow: EditText? = null
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val startDate = SimpleDateFormat("yyyy-MM-dd HH:mm ")
    val endDate = SimpleDateFormat(" HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        hs = HttpService()
        hd = Handler()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val weather: Button = findViewById<View>(R.id.btWeather) as Button
        weather.setOnClickListener(listener);

        val postMultiPart: Button = findViewById<View>(R.id.btPostMultiPart) as Button
        postMultiPart.setOnClickListener(listener);

        val postJson: Button = findViewById<View>(R.id.btPostJson) as Button
        postJson.setOnClickListener(listener);

        val postForm: Button = findViewById<View>(R.id.btPostForm) as Button
        postForm.setOnClickListener(listener);

        editShow = findViewById<View>(R.id.editTextTextMultiLine) as EditText
    }

    val listener = View.OnClickListener { view ->

        when (view.getId()) {
            R.id.btWeather -> {
                Thread {
                    var result = hs?.sendGet("http://10.168.18.61/webapplication/api/weather?locationName=新北市")
                    if (result != null) {
                        hd?.post {
                            var gson = Gson()
                            var weather = gson.fromJson(result, WeatherModel::class.java)
                            if (weather != null && weather.success){
                                var newline = "\r\n"
                                var sb = StringBuilder()
                                sb.append(weather.records.datasetDescription + " " + weather.records.location[0].locationName + newline)

                                for (status in weather.records.location[0].weatherElement) {
                                    if (status.elementName == "Wx")
                                        for(time in status.time) {
                                            sb.append(startDate.format(sdf.parse(time.startTime)) + "~" + endDate.format(sdf.parse(time.endTime)) + " " + time.parameter.parameterName + newline)
                                        }

                                    if (status.elementName == "PoP")
                                        for(time in status.time) {
                                            sb.append(" 下雨率:" + time.parameter.parameterName + "%" + newline)
                                        }

                                    if (status.elementName == "MinT")
                                        for(time in status.time) {
                                            sb.append(" 溫度:" + time.parameter.parameterName +  time.parameter.parameterUnit )
                                        }

                                    if (status.elementName == "MaxT")
                                        for(time in status.time) {
                                            sb.append(" ~ " + time.parameter.parameterName +  time.parameter.parameterUnit + newline)
                                        }

                                    if (status.elementName == "CI")
                                        for(time in status.time) {
                                            sb.append(time.parameter.parameterName + newline)
                                        }
                                }

                                editShow?.setText(sb.toString())
                            }
                        }
                        Log.i(tag, result)
                    }
                }.start()
            }
            R.id.btPostMultiPart -> {
                Thread {
                    var fileName = "aspnetcor.png"
                    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),  fileName)
                    var result = hs?.sendPostMultiPart("http://10.168.18.61/webapplication/api/weather/Upload",FileInputStream(file),fileName,mapOf("name" to "wan", "idno" to "A123456789"))
                    if (result != null) {
                        hd?.post {
                            editShow?.setText(result)
                        }
                        Log.i(tag, result)
                    }
                }.start()
            }
            R.id.btPostJson -> {
                Thread {
                    var result = hs?.sendPostJson("http://10.168.18.61/webapplication/api/weather/Upload","{\"name\":\"wang\", \"value\":25}")
                    if (result != null) {
                        hd?.post {
                            editShow?.setText(result)
                        }
                        Log.i(tag, result)
                    }
                }.start()
            }
            R.id.btPostForm -> {
                Thread {
                    var result = hs?.sendPostForm("http://10.168.18.61/webapplication/api/weather/Upload","name=wang&value=25")
                    if (result != null) {
                        hd?.post {
                            editShow?.setText(result)
                        }
                        Log.i(tag, result)
                    }
                }.start()
            }
        }
    }
}