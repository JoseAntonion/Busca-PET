package com.example.buscapet.auth.presentation

import android.content.Intent

open class SignInEventResult {
    data object OnSignInSuccess : SignInEventResult()
    data class OnSignInFail(val message: String) : SignInEventResult()
    data class OnSignInError(val message: String) : SignInEventResult()
    data class OnAccountSelectorIntent(val intent: Intent) : SignInEventResult()
}