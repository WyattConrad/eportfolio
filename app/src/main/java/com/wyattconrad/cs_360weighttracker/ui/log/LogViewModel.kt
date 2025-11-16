package com.wyattconrad.cs_360weighttracker.ui.log


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wyattconrad.cs_360weighttracker.model.Weight
import com.wyattconrad.cs_360weighttracker.data.WeightRepository
import com.wyattconrad.cs_360weighttracker.service.LoginService
import com.wyattconrad.cs_360weighttracker.utilities.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Sealed class to define the events that can be sent from the UI
sealed class LogEvent {
    data class DeleteWeight(val weight: Weight) : LogEvent()
    data class EditWeight(val weight: Weight) : LogEvent()
    data class UpdateWeight(val newWeightValue: String) : LogEvent()
    object DismissEditDialog : LogEvent()
}

@HiltViewModel
class LogViewModel @Inject constructor(// Inject your repository, which in turn uses your DAO
    private val weightRepository: WeightRepository,
    private val loginService: LoginService
) : ViewModel() {

    private val userId: Long = loginService.userId
    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


            // Observe the Flow of weights from the database
    val weights = weightRepository.getAllWeightsByUserId(userId)

    fun addWeight(weightValue: Double) {
        viewModelScope.launch {
            val weight = Weight(weight = weightValue, userId = userId)
            weightRepository.addWeight(weight)
        }
    }

}