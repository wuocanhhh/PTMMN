package com.example.receiving_test.phone

import android.telephony.SmsManager
import android.util.Log

class PhoneMessageSending() {
    companion object {
        @JvmStatic
        public fun sendSms(phoneNumber: String, message: String) {
            try {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                Log.d("PhoneMessageSending", "SMS sent.")
            } catch (e: Exception) {
                Log.d("PhoneMessageSending", "SMS failed, please try again.")
                e.printStackTrace()
            }
        }
    }
}
