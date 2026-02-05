package com.example.buscapet.home.presentation

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.domain.model.PetState
import com.example.buscapet.core.presentation.model.UiText
import com.example.buscapet.core.presentation.util.CoreUiEvent
import com.example.buscapet.data.local.PetsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val petsRepository: PetsRepository
) : ViewModel() {

    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val _uiState = MutableStateFlow(HomeState())
    val uiState
        get() = _uiState.asStateFlow()

    private val _homeEvents = Channel<CoreUiEvent>()
    val homeEvents = _homeEvents.receiveAsFlow()

    private val nameSplited = currentUser?.displayName?.split(" ")
    private val displayName = nameSplited
        ?.filterIndexed { index, _ -> index % 2 == 0 }
        ?.joinToString(" ") {
            it.capitalize(Locale.current)
        }

    init {
        _uiState.update { it.copy(currentUser = displayName, photo = currentUser?.photoUrl) }
        getMyPets()
    }

    private fun getMyPets() {
        val uid = currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                petsRepository.getPetsByOwner(uid)
                    .onStart { _uiState.update { it.copy(isLoading = true) } }
                    .catch {
                        _uiState.update { it.copy(isLoading = false) }
                        // TODO: Handle error, maybe send a UI event
                    }
                    .collect { pets ->
                        _uiState.update { it.copy(myPets = pets, isLoading = false) }
                    }
            }
        }
    }

    fun onReportExistingPet(pet: Pet) {
        if (pet.petState == PetState.LOST) {
            viewModelScope.launch {
                _homeEvents.send(
                    CoreUiEvent.ShowSnackbar(
                        UiText.DynamicString("Esta mascota ya se encuentra reportada como perdida.")
                    )
                )
            }
            return
        }

        val updatedPet = pet.copy(
            petState = PetState.LOST,
            timestamp = System.currentTimeMillis(),
            reporterId = currentUser?.uid
        )

        viewModelScope.launch {
            val success = petsRepository.insertPet(updatedPet)
            if (success) {
                _homeEvents.send(
                    CoreUiEvent.ShowSnackbar(
                        UiText.DynamicString("Reporte enviado exitosamente")
                    )
                )
            } else {
                _homeEvents.send(
                    CoreUiEvent.ShowSnackbar(
                        UiText.DynamicString("Error al guardar el reporte. Inténtalo nuevamente.")
                    )
                )
            }
        }
    }
}