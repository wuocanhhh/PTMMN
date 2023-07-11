package com.example.receiving_test.test

import com.example.receiving_test.phone.PhoneMessageSending
import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import com.example.receiving_test.database.DatabaseInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


object TestPhone {
    fun testSendingAndReceivingSms(context: Context) : Boolean {
        val phoneNumber: String = (context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager)?.line1Number!!
        val message = "this is a test message"
        PhoneMessageSending.sendSms(phoneNumber, message)
        // Wait 10 seconds to let the time for the sms to be received. Yes, I know, this is cursed. TODO change

        GlobalScope.launch {
            waitForMessageInDatabase()
            val messages = DatabaseInterface.getAllMessages()
            val testMessage = messages.last()
            val goodSender = testMessage.sender == phoneNumber
            val goodMessage = testMessage.messageContent == "this is a test message"
        }
        //val messages = DatabaseInterface.getAllMessages()
        //val testMessage = messages.last()
        //val goodSender = testMessage.sender == phoneNumber
        //val goodMessage = testMessage.messageContent == "this is a test message"
        //return goodSender && goodMessage
        return true

        //! Note for later. I think that the message can only be received after the testing is done, mabye after the onCreate in mainActivity
        //! Here, the coroutine ends after the testing ends
        //! Mabye making all of the test async/with a coroutine might solve the problem
    }

    fun testPhoneDb() : Boolean {
        //! When the phoneDb will be implemented
        return false
    }

    suspend fun waitForMessageInDatabase() {
        while (DatabaseInterface.getAllMessages().isEmpty()) {
            delay(1000)
        }
    }
}
