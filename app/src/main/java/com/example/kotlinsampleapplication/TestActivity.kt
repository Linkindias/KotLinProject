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
import com.example.kotlinsampleapplication.ViewModel.WeatherInfo
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
        Log.i(tag,"TestActivity start")
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
                            var weather = gson.fromJson(result.toString(), WeatherModel::class.java)
                            if (weather != null && weather.success){
                                var newline = "\r\n"
                                var mutableList: MutableList<WeatherInfo>  = mutableListOf()
                                var sb = StringBuilder()
                                sb.append(weather.records.datasetDescription + " " + weather.records.location[0].locationName + newline)

                                for (status in weather.records.location[0].weatherElement) {
                                    if (status.elementName == "Wx")
                                        for(time in status.time) {
                                            var info = WeatherInfo()
                                            info?.startDate = startDate.format(sdf.parse(time.startTime))
                                            info?.endDate = endDate.format(sdf.parse(time.endTime))
                                            info?.status = time.parameter.parameterName
                                            mutableList.add(info)
                                        }

                                    if (status.elementName == "PoP")
                                        for((index, time) in status.time.withIndex()) {
                                            mutableList[index].rainRate = time.parameter.parameterName
                                        }

                                    if (status.elementName == "MinT")
                                        for((index, time) in status.time.withIndex()) {
                                            mutableList[index].startTemp = time.parameter.parameterName +  time.parameter.parameterUnit
                                        }

                                    if (status.elementName == "MaxT")
                                        for((index, time) in status.time.withIndex()) {
                                            mutableList[index].endTemp = time.parameter.parameterName +  time.parameter.parameterUnit
                                        }

                                    if (status.elementName == "CI")
                                        for((index, time) in status.time.withIndex()) {
                                            mutableList[index].status2 = time.parameter.parameterName
                                        }
                                }
                                for((index, weather) in mutableList.withIndex()) {
                                    sb.append(weather.startDate + "~" + weather.endDate + newline)
                                    sb.append(weather.status + " 下雨率:" + weather.rainRate + "%" + newline)
                                    sb.append("溫度:" + weather.startTemp + " ~ " + weather.endTemp + newline)
                                    sb.append(weather.status2 + newline)
                                }
                                editShow?.setText(sb.toString())
                            }
                        }
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

