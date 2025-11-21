package com.wyattconrad.cs_360weighttracker.ui.settings

import android.telephony.SmsManager
import androidx.lifecycle.ViewModel
import com.wyattconrad.cs_360weighttracker.service.LoginService
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SettingsUiState(
    val smsEnabled: Boolean = false,
    val inAppEnabled: Boolean = false,
    val phoneNumber: String = "",
    val isSmsPermissionGranted: Boolean = false,
    val showPhoneInput: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: UserPreferencesService,
    loginService: LoginService,
    private val smsManager: SmsManager
) : ViewModel() {

    private val userId = loginService.userId

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPrefs()
    }

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
    }

    // Called AFTER permission result arrives
    fun onSmsPermissionResult(granted: Boolean) {
        if (granted) {
            prefs.putGlobalBoolean("sms_enabled", true)

            _uiState.update {
                it.copy(
                    isSmsPermissionGranted = true,
                    smsEnabled = true,
                    showPhoneInput = true
                )
            }
        } else {
            prefs.putGlobalBoolean("sms_enabled", false)

            _uiState.update {
                it.copy(
                    isSmsPermissionGranted = false,
                    smsEnabled = false,
                    showPhoneInput = false
                )
            }
        }
    }

    fun toggleInApp(enabled: Boolean) {
        prefs.putGlobalBoolean("in_app_messaging", enabled)

        _uiState.update {
            it.copy(inAppEnabled = enabled)
        }
    }

    fun updatePhoneNumber(newNumber: String) {
        prefs.putGlobalString("sms_number", newNumber)

        _uiState.update { it.copy(phoneNumber = newNumber) }

        // Send test SMS
        if (newNumber.isNotBlank()) {
            smsManager.sendTextMessage(
                newNumber,
                null,
                "Thank you for subscribing to weight tracking!",
                null,
                null
            )
        }
    }
}

