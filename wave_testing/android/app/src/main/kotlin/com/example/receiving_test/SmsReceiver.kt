package com.example.receiving_test

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import androidx.core.app.NotificationCompat
import android.telephony.SmsMessage
import android.util.Log
import io.flutter.plugin.common.MethodChannel
import android.telephony.TelephonyManager
import com.example.receiving_test.models.MessageModel

class SmsReceiver : BroadcastReceiver() {

    private lateinit var methodChannel: MethodChannel

    fun setMethodChannel(methodChannel: MethodChannel) {
        this.methodChannel = methodChannel
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_DELIVER_ACTION) {
            val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (message in smsMessages) {
                if (message.isEmail()) {continue} //! Temporary, if it is an sms sent using an email, just ignore it for now :P
                Log.d("SmsReceiver", "Message received from $senderPhoneNumber containing: ${message.messageBody}")

                // Create a new message
                val newMessage = MessageModel(
                    sender = message.originatingAddress?,
                    message = message.messageBody,
                    timestampSent = message.getTimestampMillis().toString(),
                    timestampReceived = System.currentTimeMillis().toString()
                )
                DatabaseInterface(context).addMessage(newMessage)

                MainActivity.sendMessageToFlutter(newMessage) //! To fix (don't send if the app is killed)
                showNotification(context, message) //! don't show it app is active
            }
        }
    }

    private fun showNotification(context: Context, message: SmsMessage) {
        val channelId = "sms_channel_id"
        val notificationId = 101

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "SMS Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("New SMS received")
            .setContentText("From: ${message.originatingAddress}, Message: ${message.messageBody}")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(notificationId, notification)
    }
}
