package com.example.receiving_test.test

import android.util.Log
import android.content.Context

object TestInterface {
    fun testAll(context: Context) {
        Log.d("Wave - TestInterface", "Testing begins--------------------------------------------------------")
        testDatabase()
        testPhone(context)
        Log.d("Wave - TestInterface", "Testing ends-----------------------------------------------------------")
    }

    private fun testDatabase() {
        val success1 = TestDatabase.testAddMessage()
        val success2 = TestDatabase.testGetAllMessages()
        val success3 = TestDatabase.testDeleteAllMessages()

        if (success1) {
            Log.i("Wave - Test", "Success - The test message was successfully added to the database")
        } else {
            Log.i("Wave - Test", "Error - The test message was not added to the database")
        }
        if (success2) {
            Log.i("Wave - Test", "Success - The test message was successfully fetched from the database")
        } else {
            Log.e("Wave - Test", "Error - The test message was not fetched from the database")
        }
        if (success3) {
            Log.i("Wave - Test", "Success - The test message was successfully deleted from the database")
        } else {
            Log.e("Wave - Test", "Error - The test message was not deleted from the database")
        }
    }

    private fun testPhone(context: Context) {
        val success4 = TestPhone.testSendingAndReceivingSms(context)

        if (success4) {
            Log.i("Wave - Test", "Success - The test message was successfully sent and received")
        } else {
            Log.e("Wave - Test", "Error - The test message was not sent and received")
        }
    }
}
