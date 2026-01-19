package com.example.buscapet.detail_report.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.data.local.PetsRepository
import com.example.buscapet.my_reports.domain.use_case.DeleteReportUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailReportViewModel @Inject constructor(
    private val petRepository: PetsRepository,
    private val deleteReportUseCase: DeleteReportUseCase
) : ViewModel() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    private val _petDetails: MutableStateFlow<Pet?> = MutableStateFlow(null)
    val petDetails: StateFlow<Pet?> = _petDetails.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _isFetching = MutableStateFlow(false)
    val isFetching: StateFlow<Boolean> = _isFetching.asStateFlow()

    private val _isOwner = MutableStateFlow(false)
    val isOwner: StateFlow<Boolean> = _isOwner.asStateFlow()

    sealed interface DetailUiEvent {
        data class ShowCoreEvent(val event: com.example.buscapet.core.presentation.util.CoreUiEvent) : DetailUiEvent
        object DeleteSuccess : DetailUiEvent
        data class EditReport(val petId: String) : DetailUiEvent
    }

    private val _uiEvent = Channel<DetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var currentPetId: String? = null

    fun setPetId(petId: String) {
        currentPetId = petId
        viewModelScope.launch {
            _isFetching.value = true
            val pet = petRepository.getPetById(petId)
            _petDetails.value = pet
            // Check if current user is the reporter or the owner
            _isOwner.value = currentUserId != null && (pet?.reporterId == currentUserId || pet?.ownerId == currentUserId)
            _isFetching.value = false
        }
    }

    fun onDeletePet() {
        currentPetId?.let { id ->
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
        currentPetId?.let { id ->
            viewModelScope.launch {
                _uiEvent.send(DetailUiEvent.EditReport(id))
            }
        }
    }
}