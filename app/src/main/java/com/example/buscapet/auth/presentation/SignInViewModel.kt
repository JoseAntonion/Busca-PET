package com.example.buscapet.auth.presentation

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.R
import com.example.buscapet.data.local.PetsRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val petsRepository: PetsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInState())
    val uiState
        get() = _uiState.asStateFlow()
    private val _signInChannel = Channel<SignInEventResult>()
    val signInChannel = _signInChannel.receiveAsFlow()

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.SignInButtonPressed -> startGoogleAccountSelector(event.activity)
            is SignInEvent.SignInWithData -> signInWithAccountData(event.accountData)
        }
    }

    private fun toggleLoading() = _uiState.update { it.copy(loading = !it.loading) }

    private fun startGoogleAccountSelector(activity: Activity) {
        toggleLoading()
        viewModelScope.launch {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val client = GoogleSignIn.getClient(activity, gso)
            val accountSelectorIntent = client.signInIntent
            _signInChannel.send(SignInEventResult.OnAccountSelectorIntent(accountSelectorIntent))
            //_uiState.update { it.copy(accountSelectorIntent = accountSelectorIntent) }
        }
        //startLogin.invoke(signInIntent)
    }

    private fun signInWithAccountData(accountData: Task<GoogleSignInAccount>) {
        try {
            Log.d("TAG", "finishLogin: INIT... accountData: $accountData")
            val account: GoogleSignInAccount = accountData.getResult(ApiException::class.java)
            Log.d("TAG", "finishLogin: account.idToken: ${account.idToken}")
            account.idToken?.let { token ->
                val auth = FirebaseAuth.getInstance()
                val credential = GoogleAuthProvider.getCredential(token, null)
                auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
//                        val user = auth.currentUser
//                        _signInName.value = user?.displayName
//                        Log.d("TAG", "Success signin")
//                        navController.navigate(Home)
//                        _progress.value = false
                        Log.d("TAG", "Success signin !!")
                        viewModelScope.launch {
                            _signInChannel.send(SignInEventResult.OnSignInSuccess)
                        }
                    } else {
                        Log.e("TAG", "Fail signin ${authResult.exception}")
                        viewModelScope.launch {
                            _signInChannel.send(
                                SignInEventResult.OnSignInFail(
                                    authResult.exception?.message ?: "Sign in fail"
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "Error signin ${e.message}")
            viewModelScope.launch {
                _signInChannel.send(SignInEventResult.OnSignInError(e.message ?: "Sign in error"))
            }
        }
    }

}