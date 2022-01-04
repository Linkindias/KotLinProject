package com.example.kotlinsampleapplication

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.usb.UsbManager
import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.FLAG_READER_NFC_A
import android.nfc.NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.base.Common.Companion.apk
import com.example.base.Common.Companion.execCommand
import com.example.base.Common.Companion.hideBar
import com.example.base.Common.Companion.ping
import com.example.kotlinsampleapplication.base.HttpApiServer
import com.example.kotlinsampleapplication.dal.media.MediaApi
import com.example.kotlinsampleapplication.fragment.BlankFragment
import com.example.kotlinsampleapplication.fragment.FullscreenFragment
import java.io.File
import java.io.IOException
import android.serialport.SerialPort
import java.io.OutputStream
import cn.wch.ch34xuartdriver.CH34xUARTDriver
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    val tag: String = "MainActivity"

    private val fullscreenFragment = FullscreenFragment()
    private val blackFragment = BlankFragment()

    private var httpApiServer: HttpApiServer? = null
    private var mediaApi: MediaApi? = null
    private var serialPort: SerialPort? = null
    private var driver: CH34xUARTDriver? = null

    var fragments: MutableList<Fragment> = mutableListOf()

    companion object {
        val notification = "com.example.kotlinsampleapplication.webapi"
        val localNotify = "com.example.kotlinsampleapplication.test"
    }

    var bocastReceiver: BroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            if(intent!!.getAction() == notification){
                val loadType = intent!!.getStringExtra("loadType")
                Log.i(tag, "onReceive:" + loadType)
            }
        }
    }

    var testBocastReceiver: BroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            if(intent!!.getAction() == localNotify){
                val event = intent!!.getStringExtra("key")
                Log.i(tag, "onReceive:" + event)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT >= 23) {
            val REQUEST_CODE_CONTACT = 101
            val permissions = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE)

            for (str in permissions) {
                if (checkSelfPermission(str!!) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, REQUEST_CODE_CONTACT)
                }
            }
        }

        driver = CH34xUARTDriver(getSystemService(Context.USB_SERVICE) as UsbManager?, this,"tw.edu.pccu.sce.qubo.USB_PERMISSION")
        if (!driver!!.UsbFeatureSupported()) {
            Log.e("mainActivity", "not supper CH34xUARTDriver")
        }



//        SerialPort.setSuPath("/dev/ttyS1");
//        serialPort = SerialPort(File("/dev/ttyS1"), 115200,0,0,0)
////            SerialPort.setSuPath("/dev/ttyS1");
////            serialPort = SerialPort.newBuilder(File("/dev/ttyS1"), 115200).build()
//            Log.i(tag,"serialPort connect")

        hideBar(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i(tag,"ping:" + ping())

        LocalBroadcastManager.getInstance(this).registerReceiver(testBocastReceiver, IntentFilter(localNotify))

        registerReceiver(bocastReceiver, IntentFilter(notification))

        val btActivity: Button = findViewById<View>(R.id.btActivity) as Button
        btActivity.setOnClickListener {
            val testActivity = Intent(this, TestActivity::class.java)
            startActivity(testActivity)
        }
        val btVideo: Button = findViewById<View>(R.id.btVideo) as Button
        btVideo.setOnClickListener {
            val mediaActivity = Intent(this, MediaActivity::class.java)
            startActivity(mediaActivity)
        }
        val btRoomdb: Button = findViewById<View>(R.id.btRoomDb) as Button
        btRoomdb.setOnClickListener {
            val dbActivity = Intent(this, RoomActivity::class.java)
            startActivity(dbActivity)
        }
        val btSerial: Button = findViewById<View>(R.id.btSerial) as Button
        btSerial.setOnClickListener {
            val serialActivity = Intent(this, SerialActivity::class.java)
            startActivity(serialActivity)
        }
        val btReceiver: Button = findViewById<View>(R.id.btReceiver) as Button
        btReceiver.setOnClickListener {
            val it = Intent(notification)
            it.putExtra("loadType", "media")
            sendBroadcast(it)
        }
        val btService: Button = findViewById<View>(R.id.btService) as Button
        btService.setOnClickListener {
            val serialActivity = Intent(this, BackgroundUpdateActivity::class.java)
            startActivity(serialActivity)
        }

        val btfFragment: Button = findViewById<View>(R.id.btfirstFragment) as Button
        btfFragment.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            var fullFragment = fragments.find { it == fullscreenFragment }

            if (fullFragment == null) {
                fragments.add(fullscreenFragment)
                transaction.add(R.id.fragment_container, fullscreenFragment)
            }
            else {
                fragments.forEach {
                    transaction.hide(it)
                }
                transaction.show(fullFragment)
            }
            transaction.commit()
        }
        val btsFragment: Button = findViewById<View>(R.id.btsecondFragment) as Button
        btsFragment.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            var bFragment = fragments.find { it == blackFragment }

            if (bFragment == null) {
                fragments.add(blackFragment)
                if (fragments.size > 0)
                    transaction.replace(R.id.fragment_container, blackFragment)
                else
                    transaction.add(R.id.fragment_container, blackFragment)
            }
            else {
                fragments.forEach {
                    transaction.hide(it)
                }
                transaction.show(bFragment)
            }
            transaction.commit()
        }
        val btInstall: Button = findViewById<View>(R.id.btInstall) as Button
        btInstall.setOnClickListener {
            var path = File(apk.path, "app-debug.apk").path
            Log.i(tag,path)

            var result = execCommand("/system/bin/sh pm install -r ${path}")
            if (result == "Success")
                execCommand("/system/bin/sh am start -n com.example.kotlinsampleapplication/com.example.kotlinsampleapplication.MainActivity")

//            val iInstall = Intent(Intent.ACTION_VIEW)
//            iInstall.setDataAndType(
//                FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", File(apk.path, "app-debug.apk")),
//            "application/vnd.android.package-archive")
//            startActivity(iInstall)
        }
        val btUninstall: Button = findViewById<View>(R.id.btUninstall) as Button
        btUninstall.setOnClickListener {
            execCommand("/system/bin/sh pm uninstall com.example.kotlinsampleapplication")

//            val intent = Intent(Intent.ACTION_DELETE)
//            intent.data = Uri.parse("package:com.example.kotlinsampleapplication")
//            startActivity(intent)
//            finish()
        }
        val btSurface: Button = findViewById<View>(R.id.btSurface) as Button
        btSurface.setOnClickListener {
            val surfaceActivity = Intent(this, StreamActivity::class.java)
            startActivity(surfaceActivity)
        }
        val btOpenDoor: Button = findViewById<View>(R.id.btOpenDoor) as Button
        btOpenDoor.setOnClickListener {
            val outStream: OutputStream = serialPort!!.getOutputStream()
            outStream.write(byteArrayOf(0x00, 0x96.toByte(), 0x43, 0x00, 0x06, 0x00, 0x8A.toByte(), 0x87.toByte()),100,8);
        }
        val btUnLuck: Button = findViewById<View>(R.id.btUnLuck) as Button
        btUnLuck.setOnClickListener {
            val outStream: OutputStream = serialPort!!.getOutputStream()
            outStream.write(byteArrayOf(0x00, 0x96.toByte(), 0x41, 0x00, 0x00, 0x02, 0x10, 0XCE.toByte(), 0x01),100,8);
        }
        val btLuck: Button = findViewById<View>(R.id.btLuck) as Button
        btLuck.setOnClickListener {
            val outStream: OutputStream = serialPort!!.getOutputStream()
            outStream.write(byteArrayOf(0x00, 0x96.toByte(), 0x41, 0x00, 0x00, 0x06, 0x10, 0xCC.toByte(), 0xC1.toByte()),100,8);
        }
        val btOpen : Button = findViewById<View>(R.id.btOpen) as Button
        btOpen.setOnClickListener {
            var to_send = byteArrayOf(160.toByte(), 1, 1, 162.toByte())
            var myusblist = driver!!.ResumeUsbList(); //開啟CH340設備
            if (myusblist == 0)
            {
                if (driver!!.UartInit())  //設備初始化
                {
                    if (driver!!.SetConfig(9600, 8, 1, 0, 0)) //設定參數
                    {
                        driver!!.WriteData(to_send, to_send.size); //發送訊號
//                        Log.i(tag,"Open writeData")
                        driver!!.CloseDevice();
                    }
                }
            }
        }
        val btClose : Button = findViewById<View>(R.id.btClose) as Button
        btClose.setOnClickListener {
            var to_send = byteArrayOf(160.toByte(),1,0,161.toByte())

            var myusblist = driver!!.ResumeUsbList(); //開啟CH340設備
            if (myusblist == 0)
            {
                if (driver!!.UartInit())  //設備初始化
                {
                    if (driver!!.SetConfig(9600, 8, 1, 0, 0)) //設定參數
                    {
                        driver!!.WriteData(to_send, to_send.size); //發送訊號
//                        Log.i(tag,"close writeData")
                        driver!!.CloseDevice();
                    }
                }
            }
        }
        val btNfc: Button = findViewById<View>(R.id.btNfc) as Button
        btNfc.setOnClickListener {
            val nfcActivity = Intent(this, NfcActivity::class.java)
            startActivity(nfcActivity)
        }
        try {
            mediaApi = MediaApi(this)
            httpApiServer = HttpApiServer(mediaApi!!, 8092)
            httpApiServer!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }



    fun sendBoardCast(type:String){
        val it = Intent(notification)
        it.putExtra("loadType", type)
        sendBroadcast(it)
    }

    override fun onDestroy() {
        super.onDestroy()

        if(httpApiServer != null) httpApiServer!!.stop();
        if (mediaApi != null) mediaApi = null
        if (bocastReceiver != null) unregisterReceiver(bocastReceiver)
        if (testBocastReceiver != null) LocalBroadcastManager.getInstance(this).unregisterReceiver(testBocastReceiver)
        if (serialPort != null) serialPort!!.tryClose()
    }


}



