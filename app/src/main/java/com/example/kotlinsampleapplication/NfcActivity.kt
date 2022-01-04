package com.example.kotlinsampleapplication

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.widget.EditText
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class NfcActivity : AppCompatActivity()  {
    val tag: String = "NfcActivity"
//    private var nfc: NfcAdapter? = null
//    private var handler: Handler? = null
    private var edit: EditText? = null

//    var key = byteArrayOf(0xB1.toByte(), 0x21, 0x5C, 0x89.toByte(), 0x38, 0xF5.toByte())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
        edit = findViewById<EditText>(R.id.edit_test_text);
//        nfc = NfcAdapter.getDefaultAdapter(this)?.let { it }
    }

//    private fun GetNfc() {
//        if (nfc == null) {
//            nfc = NfcAdapter.getDefaultAdapter(this)
//        }
//    }
//
//    private fun RestartNfc() {
//        DisableNfc()
//        GetNfc()
//        EnableNfc()
//    }
//
//    private fun SetupNfcResetTimer()
//    {
//        handler?.postDelayed({
//            RestartNfc()
//        },6000)
//    }
//
//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//
//        onTagDiscovered(intent?.getParcelableExtra(NfcAdapter.EXTRA_TAG))
//    }
//
//    private fun DisableNfc() {
//        if (nfc != null) {
//            nfc?.disableReaderMode(this);
//            nfc = null;
//        }
//    }
//
//    private fun EnableNfc() {
//        if (nfc != null) {
//            var options = Bundle()
//            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 500);
//            nfc?.enableReaderMode( this, this, NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS, options)
//        }
//    }
//
//    override fun onTagDiscovered(tag: Tag?) {
////        SetupNfcResetTimer()
//
//        var mifare = MifareClassic.get(tag);
//        if (mifare != null)
//        {
//            try
//            {
//                var serialno = tag?.getId()?.toString(Charsets.UTF_8)?.replace("-", "")
//
//                mifare.connect();
//
//                if (mifare.authenticateSectorWithKeyA(14, key))
//                {
//                    var blockData = mifare.readBlock(56)
//                    var account = String(blockData, 0, 10)
//                    var exp = LocalDateTime.parse("20" + String.format("%02x", blockData[10]) + "/" + String.format("%02x", blockData[11]) + "/" +String.format("%02x", blockData[12]) + " 23:59:59", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
////                        exp = LocalDateTime.parse("2050/12/31 23:59:59");
//                    var manType = String(blockData, 13, 1).trim()
//                    var version = String(blockData,14, 1).trim()
//                    var isKeyAuthPass = true;
//                    Log.i("MainActivity","a:" + account + " m:" + manType + " v:" + version)
//                    Log.i("MainActivity","d:" + exp)
//                }
//            }
//            catch (ex: Exception)
//            {
//                Log.e("MainActivity", ex.message.toString());
//                return;
//            }
//        }
//    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {

        try
        {
            if (edit?.text!!.length == 10)
            {
                var reverSeriaNo = "";
                var mySeriaNo = String.format("%02x", edit?.text.toString().toLong())
                var length = mySeriaNo.length
                for(i in 6 downTo 0 step 2) {
                    reverSeriaNo += mySeriaNo.substring(i, length)
                    length -=2
                }
                edit?.setText("")
                Log.i(tag,"S:" + reverSeriaNo.uppercase())
            }
        }
        catch (ex : Exception)
        {
            Log.e(tag,ex.message.toString())
        }

//        return onKeyUp(keyCode, event);
        return true;
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (edit != null) {
            var input = edit?.text.toString()
            when (keyCode) {
                KEYCODE_0 -> edit?.setText(input + "0")
                KEYCODE_1 -> edit?.setText(input + "1")
                KEYCODE_2 -> edit?.setText(input + "2")
                KEYCODE_3 -> edit?.setText(input + "3")
                KEYCODE_4 -> edit?.setText(input + "4")
                KEYCODE_5 -> edit?.setText(input + "5")
                KEYCODE_6 -> edit?.setText(input + "6")
                KEYCODE_7 -> edit?.setText(input + "7")
                KEYCODE_8 -> edit?.setText(input + "8")
                KEYCODE_9 -> edit?.setText(input + "9")
            }
        }
        return onKeyDown(keyCode, event);
    }
}