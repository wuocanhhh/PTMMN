package com.example.receiving_test.database

import com.example.receiving_test.models.MessageModel
import android.content.Context

object DatabaseInterface {
    private lateinit var messageDatabaseHandler: MessageDatabaseHandler

    fun initializeDatabaseContext(context: Context) {
        messageDatabaseHandler = MessageDatabaseHandler(context)
    }

    fun addMessage(message: MessageModel): Boolean {
        return messageDatabaseHandler.addMessage(message)
    }

    fun getAllMessages(): List<MessageModel> {
        return messageDatabaseHandler.getAllMessages()
    }

    fun deleteAllMessages(): Boolean {
        return messageDatabaseHandler.deleteAllMessages()
    }
}
