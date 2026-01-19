package com.example.buscapet.auth.presentation

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.R
import com.example.buscapet.auth.domain.use_case.SignInUseCase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInState())
    val uiState
        get() = _uiState.asStateFlow()
    private val _signInChannel = Channel<SignInEventResult>()
    val signInChannel = _signInChannel.receiveAsFlow()

    init {
        _uiState.update { it.copy(frontLoading = true) }
        viewModelScope.launch {
            delay(2000L)
            Log.d("TAG", "init SignInViewModel")
            val client = getGoogleSignInClient(context = context)
            val gIntent = client.silentSignIn()
            if (gIntent.isSuccessful) {
                _signInChannel.send(SignInEventResult.OnSignInSuccess)
            }
            _uiState.update { it.copy(frontLoading = false) }
        }
    }

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.SignInButtonPressed -> startGoogleAccountSelector(event.activity)
            is SignInEvent.SignInWithData -> signIn(event.accountData)
        }
    }

    private fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    private fun toggleLoading() = _uiState.update { it.copy(loading = !it.loading) }

    private fun startGoogleAccountSelector(activity: Activity) {
        toggleLoading()
        Log.d("TAG", "startGoogleAccountSelector: INIT")
        viewModelScope.launch {
            val client = getGoogleSignInClient(context = activity)
            val accountSelectorIntent = client.signInIntent
            _signInChannel.send(SignInEventResult.OnAccountSelectorIntent(accountSelectorIntent))
        }
    }

    private fun signIn(accountData: Task<GoogleSignInAccount>) {
        signInUseCase(accountData) { result ->
            viewModelScope.launch {
                if (result.success) {
                    Log.d("TAG", "signInWithAccountData: Success")
                    _signInChannel.send(SignInEventResult.OnSignInSuccess)
                } else {
                    Log.d("TAG", "signInWithAccountData: Error")
                    _signInChannel.send(SignInEventResult.OnSignInError(result.errorMessage))
                }
            }
        }
    }

}