package com.example.buscapet.auth.presentation

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

open class SignInEvent {
    data class SignInButtonPressed(val activity: Activity) : SignInEvent()
    data class SignInWithData(val accountData: Task<GoogleSignInAccount>) : SignInEvent()
}