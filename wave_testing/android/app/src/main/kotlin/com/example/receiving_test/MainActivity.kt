package com.example.receiving_test

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.EventChannel
import android.telephony.SmsManager
import android.widget.Toast
import com.example.receiving_test.database.DatabaseInterface
import android.util.Log
import android.content.Context
import android.telephony.TelephonyManager
import com.example.receiving_test.models.MessageModel


class MainActivity: FlutterActivity() {
    private val METHOD_CHANNEL = "com.example.receiving_test/method"
    private val EVENT_CHANNEL = "com.example.receiving_test/event"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val databaseInterface = DatabaseInterface(this)

        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, METHOD_CHANNEL).setMethodCallHandler {
            call, result ->
            when (call.method) {
                "getNewMessages" -> {
                    val messages = databaseInterface.getAllMessages()
                    val messageMaps = messages.map {
                        mapOf(
                            "sender" to it.sender,
                            "message" to it.message,
                            "timestampSent" to it.timestampSent,
                            "timestampReceived" to it.timestampReceived
                        )
                    }
                    result.success(messageMaps)
                }

                "sendSms" -> {
                    val message = call.argument<String>("message")
                    val phoneNumbers = call.argument<List<String>>("phoneNumbers")

                    if (phoneNumbers != null && phoneNumbers.isNotEmpty() && message != null) {
                        for (phoneNumber in phoneNumbers) {
                            sendSms(phoneNumber, message)
                            Log.d("MyApp", "Sending message to: $phoneNumber")
                        }

                        val sms = MessageModel(
                            sender = databaseInterface.createOrReturnUser("me"),
                            message = message,
                            timestampSent = System.currentTimeMillis().toString(),
                            timestampReceived = null
                        )
                        result.success(databaseInterface.addMessage(sms))
                    } else {
                        Log.d("MyApp", "Could not send SMS, messages or participants is empty")
                        result.error("UNAVAILABLE", "Could not send SMS", null)
                    }
                }

                "deleteAllMessages" -> {
                    result.success(databaseInterface.deleteAllMessages()) //! Rename the database so it doesn't look like it deletes the actual database (it's just for temporary storing)
                }

                else -> result.notImplemented()
            }
        }

        EventChannel(flutterEngine!!.dartExecutor.binaryMessenger, EVENT_CHANNEL).setStreamHandler(
            object : EventChannel.StreamHandler {
                override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                    eventSink = events
                }

                override fun onCancel(arguments: Any?) {
                    eventSink = null
                }
            })
    }

    companion object {
        private var eventSink: EventChannel.EventSink? = null

        @JvmStatic
        fun sendMessageToFlutter(message: MessageModel) {
            val messageMap = mapOf(
                "conversationId" to message.conversationId,
                "message" to message.message,
                "timestampSent" to message.timestampSent,
                "timestampReceived" to message.timestampReceived
            )
            eventSink?.success(messageMap)
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
