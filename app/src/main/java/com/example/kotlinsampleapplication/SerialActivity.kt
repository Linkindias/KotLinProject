package com.example.kotlinsampleapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.base.Common.Companion.hideBar
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface

class SerialActivity : AppCompatActivity() {
    lateinit var usbManager: UsbManager
    var device: UsbDevice? = null
    var serialPort: UsbSerialDevice? = null
    var connection: UsbDeviceConnection? = null

    val ACTION_USB_PERMISSION = "permission"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serial)

        hideBar(this)
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
                serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection)
                if (serialPort != null && serialPort!!.open()) {
                    serialPort!!.setBaudRate(115200);
                    serialPort!!.setDataBits(UsbSerialInterface.DATA_BITS_8);
                    serialPort!!.setStopBits(UsbSerialInterface.STOP_BITS_1);
                    serialPort!!.setParity(UsbSerialInterface.PARITY_NONE);
                    serialPort!!.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
//                    serialPort!!.read(mCallback);
//                    if (connection != null) {
//                        connection.onConnected();
//                    }
                }
            }
        }

    }
}