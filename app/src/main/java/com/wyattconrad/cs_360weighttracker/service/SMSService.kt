/*
 * Copyright (C) 2025 Wyatt Conrad. All rights reserved.
 *
 * This file is part of the CS-360 Weight Tracker project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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