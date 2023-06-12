package com.example.receiving_test

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import com.example.receiving_test.database.DatabaseInterface
import com.example.receiving_test.frontend_comm.FrontendSender
import com.example.receiving_test.frontend_comm.FrontendReceiver
import com.example.receiving_test.phone.PhoneMessageSending
import com.example.receiving_test.test.TestInterface

class MainActivity: FlutterActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TestInterface.testAll()

        val databaseInterface = DatabaseInterface(this)
        val methodChannelHandler = FrontendReceiver(databaseInterface)
        val eventChannelHandler = FrontendSender()

        flutterEngine?.let { engine ->
            methodChannelHandler.setupMethodChannel(engine)
            eventChannelHandler.setupEventChannel(engine)
        }
    }
}
