package com.example.buscapet.auth.presentation.sign_in

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.buscapet.R
import com.example.buscapet.data.local.PetsRepository
import com.example.buscapet.core.navigation.Home
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val petsRepository: PetsRepository
) : ViewModel(), CoroutineScope {

    private lateinit var auth: FirebaseAuth

    private val _signInName = MutableLiveData<String>()
    val signInName: LiveData<String>
        get() = _signInName

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    fun loginWithGoogle(activity: Activity?, startLogin: (Intent) -> Unit) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity!!.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(activity, gso)
        val signInIntent = client.signInIntent
        startLogin.invoke(signInIntent)
        _progress.value = true
    }

    fun finishLogin(
        googleTask: Task<GoogleSignInAccount>,
        navController: NavController
    ) {
        //try {
        Log.d("TAG", "finishLogin: INIT... googleTask: $googleTask")
        val account: GoogleSignInAccount = googleTask.getResult(ApiException::class.java)
        Log.d("TAG", "finishLogin: account.idToken: ${account.idToken}")
        account.idToken?.let { token ->
            auth = FirebaseAuth.getInstance()
            val credential = GoogleAuthProvider.getCredential(token, null)
            auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    val user = auth.currentUser
                    _signInName.value = user?.displayName
                    Log.d("TAG", "Success signin")
                    navController.navigate(Home)
                    //_progress.value = false
                } else {
                    Log.e("TAG", "unSuccess signin ${authResult.exception}")
                    //_progress.value = false
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

}