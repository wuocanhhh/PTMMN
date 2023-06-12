package com.example.receiving_test.frontend_comm

import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.util.Log
import com.example.receiving_test.database.DatabaseInterface
import com.example.receiving_test.models.MessageModel
import com.example.receiving_test.phone.PhoneMessageSending

class FrontendReceiver(private val databaseInterface: DatabaseInterface) {

    companion object {
        const val METHOD_CHANNEL = "com.example.receiving_test.frontend_comm/method"
    }

    fun setupMethodChannel(flutterEngine: FlutterEngine) {
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, METHOD_CHANNEL).setMethodCallHandler {
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
                            PhoneMessageSending.sendSms(phoneNumber, message)
                            Log.d("MyApp", "Sending message to: $phoneNumber")
                        }

                        val sms = MessageModel(
                            sender = "me",
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
                    result.success(databaseInterface.deleteAllMessages())
                }

                else -> result.notImplemented()
            }
        }
    }
}
