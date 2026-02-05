package com.example.buscapet.detail_report.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.domain.model.Treatment
import com.example.buscapet.data.local.PetsRepository
import com.example.buscapet.my_reports.domain.use_case.DeleteReportUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DetailReportViewModel @Inject constructor(
    private val petRepository: PetsRepository,
    private val deleteReportUseCase: DeleteReportUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val petId: String = savedStateHandle.get<String>("id")!!
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    private val _petDetails = MutableStateFlow<Pet?>(null)
    val petDetails: StateFlow<Pet?> = _petDetails.asStateFlow()

    private val _treatments = petRepository.getTreatmentsForPet(petId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val treatments: StateFlow<List<Treatment>> = _treatments

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isFetching = MutableStateFlow(false)
    val isFetching: StateFlow<Boolean> = _isFetching.asStateFlow()

    private val _isOwner = MutableStateFlow(false)
    val isOwner: StateFlow<Boolean> = _isOwner.asStateFlow()

    val hasActiveTreatment: StateFlow<Boolean> = treatments.map { it.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val nextMedication: StateFlow<String?> = treatments.map { calculateNextMedication(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    sealed interface DetailUiEvent {
        data class ShowCoreEvent(val event: com.example.buscapet.core.presentation.util.CoreUiEvent) : DetailUiEvent
        object DeleteSuccess : DetailUiEvent
        data class EditReport(val petId: String) : DetailUiEvent
    }

    private val _uiEvent = Channel<DetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        setPetId(petId)
    }

    private fun setPetId(petId: String) {
        viewModelScope.launch {
            _isFetching.value = true
            val pet = petRepository.getPetById(petId)
            _petDetails.value = pet
            _isOwner.value = currentUserId != null && (pet?.reporterId == currentUserId || pet?.ownerId == currentUserId)
            _isFetching.value = false
        }
    }

    private fun calculateNextMedication(treatments: List<Treatment>): String? {
        if (treatments.isEmpty()) return null

        var nextMedicationTime = Long.MAX_VALUE
        var nextMedicationName: String? = null

        val now = System.currentTimeMillis()

        treatments.forEach { treatment ->
            val treatmentEndDate = Calendar.getInstance().apply {
                timeInMillis = treatment.startDate
                add(Calendar.DAY_OF_YEAR, treatment.days)
            }.timeInMillis

            if (now < treatmentEndDate) {
                var medicationTime = treatment.startDate
                while (medicationTime < now) {
                    medicationTime += treatment.frequencyHours * 60 * 60 * 1000
                }

                if (medicationTime < nextMedicationTime) {
                    nextMedicationTime = medicationTime
                    nextMedicationName = treatment.medicationName
                }
            }
        }

        return if (nextMedicationName != null) {
            val date = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
                .format(nextMedicationTime)
            "Próxima dosis de $nextMedicationName a las $date"
        } else {
            null
        }
    }

    fun onDeletePet() {
        petId.let { id ->
            viewModelScope.launch {
                _isLoading.update { true }
                val success = deleteReportUseCase(id)

                if (success) {
                    _uiEvent.send(DetailUiEvent.DeleteSuccess)
                } else {
                    _isLoading.update { false }
                    _uiEvent.send(
                        DetailUiEvent.ShowCoreEvent(
                            com.example.buscapet.core.presentation.util.CoreUiEvent.ShowSnackbar(
                                com.example.buscapet.core.presentation.model.UiText.DynamicString("Error al eliminar el reporte")
                            )
                        )
                    )
                }
            }
        }
    }

    fun onEditPet() {
        petId.let { id ->
            viewModelScope.launch {
                _uiEvent.send(DetailUiEvent.EditReport(id))
            }
        }
    }
}
