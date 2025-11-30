package com.wyattconrad.cs_360weighttracker.service

import android.content.Context
import android.telephony.SmsManager

class SMSService(private val context: Context) {

    /**
     * Sends an SMS message to the specified phone number.
     *
     * @param phoneNumber The destination phone number (e.g., "1234567890").
     * @param message The text content of the SMS.
     */
    fun sendSMS(phoneNumber: String, message: String) : Boolean {
            return try {
                // Get the SMS Manager
                val smsManager = getSmsManager()

                // Separate the message into parts if necessary, then send the message.
                val parts = smsManager.divideMessage(message)
                if (parts.size > 1) {
                    smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
                }
                else {
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                }
                // return true if the message was sent successfully
                true
            } catch (e: Exception) {
                // return false if there was an error sending the message
                e.printStackTrace()
                false
            }
    }

    /**
     * Helper to get the correct SmsManager instance based on Android version
     */
    private fun getSmsManager(): SmsManager {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            context.getSystemService(SmsManager::class.java)
        } else {
            @Suppress("DEPRECATION")
            SmsManager.getDefault()
        }
    }
}