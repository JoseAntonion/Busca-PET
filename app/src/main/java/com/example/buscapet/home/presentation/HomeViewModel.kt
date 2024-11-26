package com.example.buscapet.home.presentation

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import com.example.buscapet.data.local.PetsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val petsRepository: PetsRepository
) : ViewModel() {

    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val _uiState = MutableStateFlow(HomeState())
    val uiState
        get() = _uiState.asStateFlow()

    private val nameSplited = currentUser?.displayName?.split(" ")
    private val displayName = nameSplited
        ?.filterIndexed { index, _ -> index % 2 == 0 }
        ?.joinToString(" ") {
            it.capitalize(Locale.current)
        }

//    data class UiState(
//        val reportDialog: Boolean = false,
//        val loading: Boolean = false,
//        val myPets: List<Pet> = emptyList(),
//        val currentUser: FirebaseUser? = null
//    )

    init {
        _uiState.update { it.copy(currentUser = displayName) }
        _uiState.update { it.copy(photo = currentUser?.photoUrl) }
    }

}