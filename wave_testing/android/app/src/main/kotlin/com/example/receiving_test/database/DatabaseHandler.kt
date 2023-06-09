package com.example.receiving_test.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.receiving_test.models.MessageModel

class MessageDatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "messageDatabase"

        private const val TABLE_MESSAGES = "messages"
        private const val KEY_MESSAGE_ID = "message_id"
        private const val KEY_SENDER = "sender"
        private const val KEY_CONTENT = "content"
        private const val KEY_TIMESTAMP_SENT = "timestamp_sent"
        private const val KEY_TIMESTAMP_RECEIVED = "timestamp_received"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""CREATE TABLE $TABLE_MESSAGES(
                $KEY_MESSAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_SENDER TEXT,
                $KEY_CONTENT TEXT,
                $KEY_TIMESTAMP_SENT TEXT,
                $KEY_TIMESTAMP_RECEIVED TEXT
                )""")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {Log.d("MessageDatabaseHandler", "Database upgrade attempt detected, but no upgrade required.")}

    // This function is used to add a message to the database
    fun addMessage(messageValues: ContentValues): Boolean {
    this.writableDatabase.use { db ->
        return try {
            // Insert the message into the database
            val insertSuccess = db.insert(TABLE_MESSAGES, null, messageValues) != -1L
            Log.d("MessageDatabaseHandler", "Insert operation success: $insertSuccess")
            insertSuccess
        } catch (e: Exception) {
            Log.e("MessageDatabaseHandler", "Error inserting message", e)
            false
        }
    }
}


    // This function is used to fetch all messages from the database
    fun getAllMessages(): List<MessageModel> {
        val messages = ArrayList<MessageModel>()

        this.readableDatabase.use { db ->
            val selectQuery = "SELECT * FROM $TABLE_MESSAGES"
            try {
                // Perform a raw SQL query to fetch all messages>
                db.rawQuery(selectQuery, null).use {
                    // Iterate over the result set
                    if (it.moveToFirst()) {
                        do {
                            // Build the message model object for each row in the result set
                            val message = MessageModel().apply {    
                                sender = it.getString(it.getColumnIndex(KEY_SENDER))
                                content = it.getString(it.getColumnIndex(KEY_CONTENT))
                                timestampSent = it.getString(it.getColumnIndex(KEY_TIMESTAMP_SENT))
                                timestampReceived = it.getString(it.getColumnIndex(KEY_TIMESTAMP_RECEIVED))
                            }
                            messages.add(message)
                        } while (it.moveToNext())
                    }
                }

                Log.d("MessageDatabaseHandler", "Number of messages fetched: ${messages.size}")
            } catch (e: Exception) {
                Log.e("MessageDatabaseHandler", "Error reading all messages", e)
            }
        }
        return messages
    }

    // This function is used to delete all messages from the database
    fun deleteAllMessages(): Boolean {
        this.writableDatabase.use { db ->
            return try {
                // Delete all messages from the database
                val numRowsDeleted = db.delete(TABLE_MESSAGES, null, null)
                Log.d("MessageDatabaseHandler", "Number of rows deleted: $numRowsDeleted")
                true
            } catch (e: Exception) {
                Log.e("MessageDatabaseHandler", "Error deleting all messages", e)
                false
            }
        }
    }
}
