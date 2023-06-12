package com.example.receiving_test.test

import com.example.receiving_test.database.DatabaseInterface

class TestDatabase() {
    companion object {
        @JvmStatic
        fun testAddMessage() : Boolean {
            val newMessage = MessageModel(
                sender = "testNumber",
                message = "testMessage",
                timestampSent = "testTimestampSent",
                timestampReceived = "testTimestampReceived"
            )
            return DatabaseInterface.addMessage(newMessage)
            //! Test if the message in the database is their
        }

        @JvmStatic
        fun testGetAllMessages() : Boolean {
            val messages = DatabaseInterface.getAllMessages()
            val testMessage = message.first()
            goodSender = message.sender == "testNumber"
            goodMessage = message.message == "testMessage"
            goodTimestampSent = message.timestampSent == "testTimestampSent"
            goodTimestampReceived = message.timestampReceived == "testTimestampReceived"
            return goodSender && goodMessage && goodTimestampSent && goodTimestampReceived
        }

        @JvmStatic
        fun testDeleteAllMessages() : Boolean {
            test1 = DatabaseInterface.deleteAllMessages
            test2 = DatabaseInterface.getAllMessages().isEmpty()
            return test1 && test2
        }
    }
}
