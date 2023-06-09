package com.example.receiving_test.database

import com.example.receiving_test.models.MessageModel
import android.content.Context

class DatabaseInterface(context: Context) {
    val messageDatabaseHandler = MessageDatabaseHandler(context)

    fun addMessage(message: MessageModel): Boolean {
        val messageValues = ContentValues().apply {
            put(MessageDatabaseHandler.KEY_SENDER, message.sender)
            put(MessageDatabaseHandler.KEY_CONTENT, message.content)
            put(MessageDatabaseHandler.KEY_TIMESTAMP, message.timestamp)
        }
        return messageDatabaseHandler.addMessage(messageValues)
    }


    fun getAllMessages(): List<MessageModel> {
        return messageDatabaseHandler.getAllMessages()
    }

    fun deleteAllMessages(): Boolean {
        return messageDatabaseHandler.deleteAllMessages()
    }
}
