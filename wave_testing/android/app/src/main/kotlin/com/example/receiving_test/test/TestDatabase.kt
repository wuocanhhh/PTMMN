package com.example.receiving_test.test

import com.example.receiving_test.database.DatabaseInterface
import com.example.receiving_test.models.MessageModel
import android.util.Log
import com.example.receiving_test.MainActivity
import com.example.receiving_test.phone.PhoneMessageSending

object TestDatabase {
    fun testAddMessage() : Boolean {
        val newMessage = MessageModel(
            sender = "testNumber",
            messageContent = "testMessage",
            timestampSent = "testTimestampSent",
            timestampReceived = "testTimestampReceived"
        )
        return DatabaseInterface.addMessage(newMessage)
    }

    fun testGetAllMessages() : Boolean {
        val messages = DatabaseInterface.getAllMessages()
        val testMessage = messages.last()
        val goodSender = testMessage.sender == "testNumber"
        val goodMessage = testMessage.messageContent == "testMessage"
        val goodTimestampSent = testMessage.timestampSent == "testTimestampSent"
        val goodTimestampReceived = testMessage.timestampReceived == "testTimestampReceived"

        return goodSender && goodMessage && goodTimestampSent && goodTimestampReceived
    }

    fun testDeleteAllMessages() : Boolean {
        val test1 = DatabaseInterface.deleteAllMessages()
        val test2 = DatabaseInterface.getAllMessages().isEmpty()
        return test1 && test2
    }
}
