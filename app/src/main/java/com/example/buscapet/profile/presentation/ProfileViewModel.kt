package com.example.buscapet.profile.presentation

import androidx.lifecycle.ViewModel
import com.example.buscapet.data.local.PetsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: PetsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState = _uiState.asStateFlow()

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

}