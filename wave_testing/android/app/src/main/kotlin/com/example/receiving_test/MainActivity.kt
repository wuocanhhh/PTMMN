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
import com.example.receiving_test.models.ConversationModel
import com.example.receiving_test.models.MessageModel
import com.example.receiving_test.models.ParticipantModel
import com.example.receiving_test.models.UserModel


class MainActivity: FlutterActivity() {
    private val METHOD_CHANNEL = "com.example.receiving_test/method"
    private val EVENT_CHANNEL = "com.example.receiving_test/event"
    private val smsReceiver = SmsReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val databaseInterface = DatabaseInterface(this)

        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, METHOD_CHANNEL).setMethodCallHandler {
            call, result ->
            when (call.method) {
                "getConversation" -> {
                    val conversationId = call.argument<Int>("conversationId")
                    if (conversationId != null) {
                        val messageModels = databaseInterface.getMessagesFromConversation(conversationId)
                        val messageMaps = messageModels.map {
                            mapOf(
                                "messageId" to it.messageId,
                                "conversationId" to it.conversationId,
                                "senderId" to it.senderId,
                                "message" to it.message,
                                "timestamp" to it.timestamp
                            )
                        }
                        result.success(messageMaps)
                    } else {
                        Log.d("MyApp", "Conversation ID cannot be null")
                        result.error("INVALID_ARGUMENT", "Conversation ID cannot be null", null)
                    }
                }

                "sendSms" -> {
                    val message = call.argument<String>("message")
                    val conversationId = call.argument<Int>("conversationId")
                    val users = databaseInterface.getAllUsersInConversation(conversationId!!)

                    if (users.isNotEmpty() && message != null) {
                        for (user in users) {
                            val userPhoneNumber = user.phoneNumber
                            if (userPhoneNumber != "me") { // Do not send message to yourself
                                sendSms(userPhoneNumber, message)
                                Log.d("MyApp", "Sending message to: $userPhoneNumber")
                            }
                        }

                        val sms = MessageModel(
                            messageId = 0, // Replace this with proper ID generation logic
                            conversationId = conversationId!!,
                            senderId = databaseInterface.createOrReturnUser("me"),
                            message = message,
                            timestamp = System.currentTimeMillis().toString()
                        )
                        databaseInterface.addMessage(sms)
                        
                        // Return a map containing messageId, senderId, and timestamp
                        val resultMap = mapOf<String, Any>(
                            "messageId" to sms.messageId,
                            "senderId" to sms.senderId,
                            "timestamp" to sms.timestamp
                        )
                        result.success(resultMap)
                    } else {
                        Log.d("MyApp", "Could not send SMS, messages or participants is empty")
                        result.error("UNAVAILABLE", "Could not send SMS", null)
                    }
                }

                "getAllConversations" -> {
                    val conversationModels = databaseInterface.getAllConversations()
                    val conversationMaps = conversationModels.map {
                        mapOf(
                            "conversationId" to it.conversationId,
                            "conversationName" to it.conversationName
                        )
                    }
                    result.success(conversationMaps)
                }

                "createNewConversation" -> {
                    val conversationName = call.argument<String>("conversationName")
                    result.success(databaseInterface.createNewConversation(conversationName!!))
                }

                "createNewUser" -> {
                    val phoneNumber = call.argument<String>("phoneNumber")
                    result.success(databaseInterface.createOrReturnUser(phoneNumber!!))
                }

                "createNewParticipant" -> {
                    val userId = call.argument<Int>("userId")
                    val conversationId = call.argument<Int>("conversationId")
                    result.success(databaseInterface.createNewParticipant(userId!!, conversationId!!))
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
        fun sendConversationToFlutter(conversation: ConversationModel) {
            Log.d("MyApp", "Conversation was apparently been received by MainActivity")
            val conversationModel: ConversationModel = conversation
            val conversationMap = mapOf(
                "conversationId" to conversationModel.conversationId,
                "conversationName" to conversationModel.conversationName
            )
            eventSink?.success(conversationMap)
        }

        @JvmStatic
        fun sendMessageToFlutter(message: MessageModel) {
            val messageMap = mapOf(
                "messageId" to message.messageId,
                "conversationId" to message.conversationId,
                "senderId" to message.senderId,
                "message" to message.message,
                "timestamp" to message.timestamp
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
