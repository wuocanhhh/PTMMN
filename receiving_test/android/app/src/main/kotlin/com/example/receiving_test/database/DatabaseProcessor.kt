package com.example.receiving_test.database

import com.example.receiving_test.models.ConversationModel
import com.example.receiving_test.models.MessageModel
import com.example.receiving_test.models.ParticipantModel
import com.example.receiving_test.models.UserModel
import android.util.Log
import android.content.Context

class DatabaseProcessor(context: Context) {
    val messageDatabaseHandler = MessageDatabaseHandler(context)

    fun addMessage(message: MessageModel) {
        messageDatabaseHandler.addMessage(message)
    }

    fun getMessagesFromConversation(conversationId: Int): List<MessageModel> {
        return messageDatabaseHandler.getMessagesFromConversation(conversationId)
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
