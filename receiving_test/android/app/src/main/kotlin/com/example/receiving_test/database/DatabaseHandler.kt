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
        private const val KEY_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""CREATE TABLE $TABLE_MESSAGES(
                $KEY_MESSAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_SENDER TEXT,
                $KEY_CONTENT TEXT,
                $KEY_TIMESTAMP TEXT
                )""")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {Log.d("MessageDatabaseHandler", "Database upgrade attempt detected, but no upgrade required.")}

    // This function is used to add a message to the database
    fun addMessage(message: MessageModel): Boolean {

        this.writableDatabase.use { db ->
            val contentValues = ContentValues().apply {
                put(KEY_SENDER, message.sender)
                put(KEY_CONTENT, message.content)
                put(KEY_TIMESTAMP, message.timestamp)
            }
            return try {
                // Insert the message into the database
                val insertSuccess = db.insert(TABLE_MESSAGES, null, contentValues) != -1L
                Log.d("MessageDatabaseHandler", "Insert operation success: $insertSuccess")
                return insertSuccess
            } catch (e: Exception) {
                Log.e("MessageDatabaseHandler", "Error inserting message", e)
                return false
            }
        }
    }

    // This function is used to fetch all messages from the database
    fun getAllMessages(): List<MessageModel> {
        val messages = ArrayList<MessageModel>()

        this.readableDatabase.use { db ->
            val selectQuery = "SELECT * FROM $TABLE_MESSAGES"
            try {
                // Perform a raw SQL query to fetch all messages
                db.rawQuery(selectQuery, null).use { cursor ->
                    // Iterate over the result set
                    if (cursor.moveToFirst()) {
                        do {
                            // Build the message model object for each row in the result set
                            val message = MessageModel().apply {    
                                id = cursor.getInt(cursor.getColumnIndex(KEY_MESSAGE_ID))
                                sender = cursor.getString(cursor.getColumnIndex(KEY_SENDER))
                                content = cursor.getString(cursor.getColumnIndex(KEY_CONTENT))
                                timestamp = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP))
                            }
                            messages.add(message)
                        } while (cursor.moveToNext())
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
                // Delete all messages from the 
                val numRowsDeleted = db.delete(TABLE_MESSAGES, null, null)
                //! TODO Is it possible to merge the line above and below this one?
                Log.d("MessageDatabaseHandler", "Number of rows deleted: $numRowsDeleted")
                return true
            } catch (e: Exception) {
                Log.e("MessageDatabaseHandler", "Error deleting all messages", e)
                return false
            }
        }
    }
}
