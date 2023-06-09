package com.example.receiving_test.models

data class SmsModel(
    var sender: String = "",
    var message: String = "",
    var timestampSent: String = "",
    var timestampReceived: String = ""
)
