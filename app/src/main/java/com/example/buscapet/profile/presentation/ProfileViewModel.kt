package com.example.buscapet.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.auth.domain.use_case.LogoutUseCase
import com.example.buscapet.data.local.PetsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: PetsRepository,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState = _uiState.asStateFlow()

    private val _isLoggingOut = MutableStateFlow(false)
    val isLoggingOut = _isLoggingOut.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    init {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userName = currentUser?.displayName
        val userEmail = currentUser?.email
        val userImage = currentUser?.photoUrl

        _uiState.value = ProfileState(
            name = userName,
            email = userEmail,
            photo = userImage
        )
    }

    fun onLogout() {
        viewModelScope.launch {
            _isLoggingOut.update { true }
            logoutUseCase()
            _isLoggingOut.update { false }
            _logoutEvent.emit(Unit)
        }
    }

}