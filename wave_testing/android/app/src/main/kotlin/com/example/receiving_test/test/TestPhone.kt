package com.example.receiving_test.test

import com.example.receiving_test.phone.PhoneMessageSending

class TestPhone() {
    companion object {
        @JvmStatic
        fun testSendingAndReceivingSms() : Boolean {
            val phoneNumber = "" //! ATTENTION DELETE THE PHONE NUMBER BEFORE PUSHING NAD CHANGE TO USE ENV AT SOME POINT
            val message = "this is a test message"
            PhoneMessageSending.sendSms(phoneNumber, message)
            //! Add logic to see if the receive message is the good one
            true
        }

        @JvmStatic
        fun testPhoneDb() : Boolean {
            //! WHen the phoneDb will be implemented
        }
    }
}
