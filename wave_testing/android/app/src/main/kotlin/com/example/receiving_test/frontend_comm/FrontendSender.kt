package com.example.receiving_test.frontend_comm

import io.flutter.plugin.common.EventChannel
import com.example.receiving_test.models.MessageModel
import io.flutter.embedding.engine.FlutterEngine

class FrontendSender {
    companion object {
        const val EVENT_CHANNEL = "com.example.receiving_test.frontend_comm/event"
        private var eventSink: EventChannel.EventSink? = null
        fun sendMessageToFlutter(message: MessageModel) {
            val messageMap = mapOf(
                "messageContent" to message.messageContent,
                "sender" to message.sender,
                "timestampSent" to message.timestampSent,
                "timestampReceived" to message.timestampReceived
            )
            eventSink?.success(messageMap)
        }
    }

    fun setupEventChannel(flutterEngine: FlutterEngine) {
        EventChannel(flutterEngine.dartExecutor.binaryMessenger, EVENT_CHANNEL).setStreamHandler(
            object : EventChannel.StreamHandler {
                override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                    eventSink = events
                }

                override fun onCancel(arguments: Any?) {
                    eventSink = null
                }
            })
    }
}
