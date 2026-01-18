package com.example.buscapet.auth.data.repository

import android.content.Context
import com.example.buscapet.R
import com.example.buscapet.auth.domain.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthRepository {
    override suspend fun logout() {
        // 1. Cerrar sesión en Firebase
        FirebaseAuth.getInstance().signOut()
        
        try {
            // 2. Cerrar sesión en Google Client para limpiar la cache del dispositivo
            // Esto asegura que 'silentSignIn' falle en el próximo inicio
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            googleSignInClient.signOut().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}