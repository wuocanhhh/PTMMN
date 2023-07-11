package com.example.receiving_test.phone

import android.telephony.SmsManager
import android.util.Log

object PhoneMessageSending {
    public fun sendSms(phoneNumber: String, message: String) {
        Log.d("Wave - PhoneMessageSending", "Sending message to: $phoneNumber")
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Log.d("Wave - PhoneMessageSending", "SMS sent.")
        } catch (e: Exception) {
            Log.d("Wave - PhoneMessageSending", "SMS failed, please try again.")
            e.printStackTrace()
        }
    }
}
