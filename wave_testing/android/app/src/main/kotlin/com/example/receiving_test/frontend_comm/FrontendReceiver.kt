package com.example.receiving_test.frontend_comm

import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.util.Log
import com.example.receiving_test.database.DatabaseInterface
import com.example.receiving_test.models.MessageModel
import com.example.receiving_test.phone.PhoneMessageSending

class FrontendReceiver() {

    companion object {
        const val METHOD_CHANNEL = "com.example.receiving_test.frontend_comm/method"
    }

    fun setupMethodChannel(flutterEngine: FlutterEngine) {
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, METHOD_CHANNEL).setMethodCallHandler {
            call, result ->
            when (call.method) {
                "getNewMessages" -> {
                    Log.d("Wave - FrontendReceiver", "Received method call from flutter to get all new messages")
                    val messages = DatabaseInterface.getAllMessages()
                    val messageMaps = messages.map {
                        mapOf(
                            "sender" to it.sender,
                            "messageContent" to it.messageContent,
                            "timestampSent" to it.timestampSent,
                            "timestampReceived" to it.timestampReceived
                        )
                    }
                    result.success(messageMaps)
                }

                "sendSms" -> {
                    val message = call.argument<String>("message")
                    val phoneNumbers = call.argument<List<String>>("phoneNumbers")
                    Log.d("Wave - FrontendReceiver", "Received method call from flutter to send message")

                    //! These checks might not be necessary if they are checked in the dart code
                    if (phoneNumbers != null && phoneNumbers.isNotEmpty() && message != null) {
                        for (phoneNumber in phoneNumbers) {
                            PhoneMessageSending.sendSms(phoneNumber, message)
                        }
                        val sms = MessageModel(
                            sender = "me",
                            messageContent = message,
                            timestampSent = System.currentTimeMillis().toString(),
                            timestampReceived = null
                        )
                        result.success(DatabaseInterface.addMessage(sms))
                    } else {
                        Log.d("Wave - FrontendReceiver", "Could not send SMS, messages or participants is empty")
                        result.error("UNAVAILABLE", "Could not send SMS", null)
                    }
                }

                //! Put this with getNewMessages? But then the return of the delete function has no purpose
                "deleteAllTemporaryMessages" -> {
                    Log.d("Wave - FrontendReceiver", "Received method call from flutter to delete all temporary messages")
                    result.success(DatabaseInterface.deleteAllMessages())
                }

                else -> result.notImplemented()
            }
        }
    }
}
