package com.example.buscapet.ui.screens.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buscapet.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class LoginViewModel : ViewModel(), CoroutineScope {

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

    fun finishLogin(googleTask: Task<GoogleSignInAccount>, signedId: () -> Unit) {
        //try {
            Log.d("TAG", "finishLogin: INIT... googleTask: $googleTask")
            val account: GoogleSignInAccount = googleTask.getResult(ApiException::class.java)
            Log.d("TAG", "finishLogin: account.idToken: ${account.idToken}")
            account.idToken?.let {
                auth = FirebaseAuth.getInstance()
                val credential = GoogleAuthProvider.getCredential(it, null)
                auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        val user = auth.currentUser
                        _signInName.value = user?.displayName
                        Log.d("TAG", "Success signin")
                        signedId.invoke()
                        //_progress.value = false
                    } else {
                        Log.e("TAG", "unSuccess signin")
                        //_progress.value = false
                    }
                }
            }
        //} catch (e: Exception) {
        //    Log.e("TAG", "error signin: ${e.message} ${e.localizedMessage} ${e.stackTrace} ${e.cause}")
        //    _progress.value = false
        //    _signInName.value = e.message
        //}
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

}