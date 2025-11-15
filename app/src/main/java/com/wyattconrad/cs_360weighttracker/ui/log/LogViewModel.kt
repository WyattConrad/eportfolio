package com.wyattconrad.cs_360weighttracker.ui.log


import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.wyattconrad.cs_360weighttracker.WeightTrackerApplication
import com.wyattconrad.cs_360weighttracker.model.Weight
import com.wyattconrad.cs_360weighttracker.repo.WeightRepository
import com.wyattconrad.cs_360weighttracker.service.LoginService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

// Sealed class to define the events that can be sent from the UI
sealed class LogEvent {
    data class DeleteWeight(val weight: Weight) : LogEvent()
    data class EditWeight(val weight: Weight) : LogEvent()
    data class UpdateWeight(val newWeightValue: String) : LogEvent()
    object DismissEditDialog : LogEvent()
}

class LogViewModel(// Inject your repository, which in turn uses your DAO
    private val weightRepository: WeightRepository,
    loginService: LoginService
) : ViewModel() {

    private val _state = MutableStateFlow(LogState())
    val state: StateFlow<LogState> = _state.asStateFlow()
    private val userId: Long = loginService.userId


    init {

        // Make sure user id is valid
        if(userId != 0L) {

            // Observe the Flow of weights from the database
            weightRepository.getAllWeightsByUserId(userId)
                .onEach { weights ->
                    _state.value = _state.value.copy(
                        weights = weights
                    )
                }
                .launchIn(viewModelScope)
        }
    }

    // Define ViewModel factory in a companion object
    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                val loginService = LoginService(application.applicationContext)


                return LogViewModel(
                    (application as WeightTrackerApplication).weightRepository,
                        loginService = loginService
                ) as T
            }
        }
    }

    /**
     * Handles events sent from the UI.
     */
    fun onEvent(event: LogEvent) {
        when (event) {
            is LogEvent.DeleteWeight -> {
                viewModelScope.launch {
                    // Perform the delete operation in a coroutine
                    weightRepository.deleteWeight(event.weight)
                }
            }
            // When the user clicks the pencil icon, set the state
            is LogEvent.EditWeight -> {
                _state.value = _state.value.copy(weightBeingEdited = event.weight)
            }
            // When the user confirms the new weight in the dialog
            is LogEvent.UpdateWeight -> {
                // Ensure there is a weight being edited
                _state.value.weightBeingEdited?.let { currentWeight ->
                    val newWeightValue = event.newWeightValue.toDoubleOrNull() ?: currentWeight.weight
                    val updatedWeight = currentWeight.copy(weight = newWeightValue)
                    viewModelScope.launch {
                        weightRepository.updateWeight(updatedWeight)
                    }
                }
                // Dismiss the dialog after updating
                _state.value = _state.value.copy(weightBeingEdited = null)
            }
            // When the user cancels the dialog
            is LogEvent.DismissEditDialog -> {
                _state.value = _state.value.copy(weightBeingEdited = null)
            }
        }
    }
}

data class LogState(
    val weights: List<Weight> = emptyList(),
    val weightBeingEdited: Weight? = null
)