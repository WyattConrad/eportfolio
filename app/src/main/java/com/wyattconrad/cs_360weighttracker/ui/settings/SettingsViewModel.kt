package com.wyattconrad.cs_360weighttracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wyattconrad.cs_360weighttracker.service.SMSService
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI State for Settings
data class SettingsUiState(
    val smsEnabled: Boolean = false,
    val inAppEnabled: Boolean = false,
    val phoneNumber: String = "",
    val isSmsPermissionGranted: Boolean = false,
    val showPhoneInput: Boolean = false
)

// ViewModel for Settings
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: UserPreferencesService,
    private val smsService: SMSService
) : ViewModel() {

    // StateFlow to hold the UI state
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    // SharedFlow to hold events
    private val _events = MutableSharedFlow<SettingsEvent>()
    val events = _events.asSharedFlow()

    // Initialize the ViewModel
    init {
        loadPrefs()
    }

    // Load the settings from the preferences
    private fun loadPrefs() {
        val smsEnabled = prefs.getGlobalBoolean("sms_enabled", false)
        val inApp = prefs.getGlobalBoolean("in_app_messaging", false)
        val number = prefs.getGlobalString( "sms_number", "") ?: ""

        _uiState.value = SettingsUiState(
            smsEnabled = smsEnabled,
            inAppEnabled = inApp,
            phoneNumber = number,
            showPhoneInput = smsEnabled
        )
    }

    // Called by UI when SMS toggle is changed
    fun toggleSms(enabled: Boolean) {
        // UI immediately updates the toggle
        _uiState.update {
            it.copy(
                smsEnabled = enabled,
                showPhoneInput = enabled
            )
        }
        prefs.putGlobalBoolean("sms_enabled", enabled)
    }

    // Called by UI when In-App toggle is changed
    fun toggleInApp(enabled: Boolean) {
        prefs.putGlobalBoolean("in_app_messaging", enabled)

        _uiState.update {
            it.copy(inAppEnabled = enabled)
        }
    }

    // Called by UI when phone number is changed
    fun onPhoneNumberChange(newNumber: String) {
        // update state
        _uiState.update { it.copy(phoneNumber = newNumber) }

        // validate + send test SMS automatically
        if (isValidPhoneNumber(newNumber)) {
            // persist it
            prefs.putGlobalString("sms_number", newNumber)
            // Send a test
            sendTestSms(newNumber)
        }
    }

    // Helper to validate a phone number
    private fun isValidPhoneNumber(number: String): Boolean {
        // Basic US validation — customize per your needs
        val digits = number.filter { it.isDigit() }
        return digits.length == 10
    }

    // Send a test SMS
    private fun sendTestSms(phone: String) {
        viewModelScope.launch {
            val success = smsService.sendSMS(phone, "Thank you for subscribing to the Weight Tracker SMS notifications.")

            if (success) {
                _events.emit(SettingsEvent.SmsSent)
            } else {
                _events.emit(SettingsEvent.SmsFailed)
            }
        }
    }
}

// Class to hold settings events
sealed class SettingsEvent {
    object SmsFailed : SettingsEvent()
    object SmsSent : SettingsEvent()
}