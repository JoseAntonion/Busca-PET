package com.example.buscapet.auth.domain.use_case

import android.util.Log
import com.example.buscapet.auth.domain.model.SignInResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class SignInUseCase @Inject constructor() {
    operator fun invoke(
        accountData: Task<GoogleSignInAccount>,
        result: (SignInResult) -> Unit
    ) {
        try {
            Log.d("TAG", "SignInUseCase: INIT... accountData: ${accountData.isSuccessful}")
            val account: GoogleSignInAccount = accountData.getResult(ApiException::class.java)
            Log.d("TAG", "SignInUseCase: account.idToken: ${account.idToken}")
            account.idToken?.let { token ->
                val auth = FirebaseAuth.getInstance()
                val credential = GoogleAuthProvider.getCredential(token, null)
                auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        Log.d("TAG", "SignInUseCase: Success SignIn !!")
                        result(SignInResult(success = true))
                    } else {
                        Log.e("TAG", "SignInUseCase: Fail signin ${authResult.exception}")
                        result(
                            SignInResult(
                                success = false,
                                errorMessage = authResult.exception?.message ?: "SignIn fail"
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "SignInUseCase: Error signin ${e.message}")
            Log.e("TAG", "SignInUseCase: Error signin ${e.cause}")
            Log.e("TAG", "SignInUseCase: Error signin ${e.stackTrace}")
            result(
                SignInResult(
                    success = false,
                    errorMessage = e.message ?: "SignIn error"
                )
            )
        }
    }
}