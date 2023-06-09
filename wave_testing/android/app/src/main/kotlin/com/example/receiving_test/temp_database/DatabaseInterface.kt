package com.example.receiving_test.database

import com.example.receiving_test.models.ConversationModel
import com.example.receiving_test.models.MessageModel
import com.example.receiving_test.models.ParticipantModel
import com.example.receiving_test.models.UserModel
import android.content.Context

class DatabaseInterface(context: Context) {
    private val databaseProcessor = DatabaseProcessor(context)

    fun addMessage(message: MessageModel) {
        databaseProcessor.addMessage(message)
    }

    fun getMessagesFromConversation(conversationId: Int): List<MessageModel> {
        return databaseProcessor.getMessagesFromConversation(conversationId)
    }

    fun getAllConversations(): List<ConversationModel> {
        return databaseProcessor.getAllConversations()
    }

    fun getAllUsersInConversation(conversationId: Int) : List<UserModel> {
        return databaseProcessor.getAllUsersInConversation(conversationId)
    }

    fun getConversationId(senderId: Int): Int? {
        return databaseProcessor.getConversationId(senderId)
    }

    fun createNewConversation(conversationName: String): Int {
        return databaseProcessor.createNewConversation(conversationName)
    }

    fun createOrReturnUser(phoneNumber: String): Int {
        return databaseProcessor.createOrReturnUser(phoneNumber)
    }

    fun createNewParticipant(userId: Int, conversationId: Int): Boolean {
        return databaseProcessor.createNewParticipant(userId, conversationId)
    }

    fun getUser(userId: Int): UserModel? {
        return databaseProcessor.getUser(userId)
    }
}
