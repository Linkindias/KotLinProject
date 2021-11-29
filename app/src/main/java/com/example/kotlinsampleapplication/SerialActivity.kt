package com.example.kotlinsampleapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface

class SerialActivity : AppCompatActivity() {
    lateinit var usbManager: UsbManager
    var device: UsbDevice? = null
    var serial: UsbSerialDevice? = null
    var connection: UsbDeviceConnection? = null

    val ACTION_USB_PERMISSION = "permission"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serial)
    }

    private fun startUsbConnection() {

    }

    private fun sendData() {

    }

    private fun disconnection() {

    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.action!! == ACTION_USB_PERMISSION) {
                connection = usbManager.openDevice(device)
                serial = UsbSerialDevice.createUsbSerialDevice(device, connection)
                if (serial != null) {
                    if (serial!!.open()){
                        serial!!.setBaudRate(9600)
                        serial!!.setDataBits(UsbSerialInterface.DATA_BITS_8)
//                        serial!!.setStopBits(usb)
                    }
                }
            }
        }
    }
}