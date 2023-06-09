package com.example.receiving_test.frontend_comm

import io.flutter.plugin.common.EventChannel
import com.example.receiving_test.models.MessageModel
import io.flutter.embedding.engine.FlutterEngine

class EventChannelHandler {

    companion object {
        const val EVENT_CHANNEL = "com.example.receiving_test.frontend_comm/event"
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
