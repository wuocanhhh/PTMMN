package com.example.receiving_test

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import com.example.receiving_test.database.DatabaseInterface
import android.telephony.SmsManager
import android.widget.Toast
import android.util.Log
import com.example.receiving_test.frontend_comm.FrontedSender
import com.example.receiving_test.frontend_comm.FrontedReceiver

class MainActivity: FlutterActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val databaseInterface = DatabaseInterface(this)
        val methodChannelHandler = FrontedReceiver(databaseInterface)
        val eventChannelHandler = FrontedSender()

        flutterEngine?.let { engine ->
            methodChannelHandler.setupMethodChannel(engine)
            eventChannelHandler.setupEventChannel(engine)
        }
    }

    private fun sendSms(phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "SMS sent.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "SMS failed, please try again.", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }   
}
