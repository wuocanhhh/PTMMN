package com.example.receiving_test.database

import com.example.receiving_test.models.ConversationModel
import com.example.receiving_test.models.MessageModel
import com.example.receiving_test.models.ParticipantModel
import com.example.receiving_test.models.UserModel
import android.util.Log
import android.content.Context

class DatabaseProcessor(context: Context) {
    val messageDatabaseHandler = MessageDatabaseHandler(context)

    fun addMessage(message: MessageModel): Boolean {
        val messageValues = ContentValues().apply {
            put(KEY_CONVERSATION_ID, message.conversationId)
            put(KEY_SENDER_ID, message.senderId)
            put(KEY_CONTENT, message.message)
            put(KEY_TIMESTAMP, message.timestamp)
        }
        return messageDatabaseHandler.addMessage(messageValues)
    }

    fun getMessagesFromConversation(conversationId: Int): List<MessageModel> {
        val messageCursor = databaseHandler.getMessagesFromConversation(conversationId)
        val messageList = ArrayList<MessageModel>()

        while (messageCursor.moveToNext()) {
            val message = MessageModel()
            message.messageId = Integer.parseInt(messageCursor.getString(messageCursor.getColumnIndex(KEY_MESSAGE_ID)))
            message.conversationId = Integer.parseInt(messageCursor.getString(messageCursor.getColumnIndex(KEY_CONVERSATION_ID)))
            message.senderId = Integer.parseInt(messageCursor.getString(messageCursor.getColumnIndex(KEY_SENDER_ID)))
            message.message = messageCursor.getString(messageCursor.getColumnIndex(KEY_CONTENT))
            message.timestamp = messageCursor.getString(messageCursor.getColumnIndex(KEY_TIMESTAMP))
            messageList.add(message)
        }
        messageCursor.close()

        return messageList
    }

    fun getAllConversations(): List<ConversationModel> {
        return messageDatabaseHandler.getAllConversations()
    }

    fun getAllUsersInConversation(conversationId: Int) : List<UserModel> {
        return messageDatabaseHandler.getAllUsersInConversation(conversationId)
    }

    fun getConversationId(senderId: Int): Int? {
        return messageDatabaseHandler.getConversationId(senderId)
    }

    fun createNewConversation(conversationName: String): Int {
        return messageDatabaseHandler.createNewConversation(conversationName)
    }

    fun createOrReturnUser(phoneNumber: String): Int {
        return messageDatabaseHandler.createOrReturnUser(phoneNumber)
    }

    fun createNewParticipant(userId: Int, conversationId: Int): Boolean {
        return messageDatabaseHandler.createNewParticipant(userId, conversationId)
    }

    fun getUser(userId: Int): UserModel? {
        return messageDatabaseHandler.getUser(userId)
    }

}
