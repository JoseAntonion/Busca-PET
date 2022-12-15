package com.example.buscapet.ui.screens.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.buscapet.R
import com.example.buscapet.ui.navigation.NavItem
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

class LoginViewModel() : ViewModel(),
    CoroutineScope {

    private lateinit var auth: FirebaseAuth
    private val _logginIn = MutableLiveData<Boolean>()
    val logginIn: LiveData<Boolean>
        get() = _logginIn
    private val _checkingSession = MutableLiveData<Boolean>()
    val checkingSession: LiveData<Boolean>
        get() = _checkingSession

    //var checkingSession = MutableStateFlow(false)

    init {
        _checkingSession.value = true
        val hasSession = FirebaseAuth.getInstance().currentUser
        _checkingSession.value = hasSession != null
    }

    fun checkSession(navController: NavController) {
        _checkingSession.value = true
        val hasSession = FirebaseAuth.getInstance().currentUser
        if (hasSession != null) {
            try {
                navController.navigate(NavItem.LastReportNavItem.screenRoute)
            }catch (e:Exception){
                val wea = e.message
            }
        } else {
            _checkingSession.value = false
        }
    }

    fun loginWithGoogle(activity: Activity?, startLogin: (Intent) -> Unit) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity!!.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(activity, gso)
        val signInIntent = client.signInIntent
        startLogin.invoke(signInIntent)
        _logginIn.value = true
    }

    fun finishLogin(googleTask: Task<GoogleSignInAccount>, navController: NavController) {
        try {
            val account: GoogleSignInAccount = googleTask.getResult(ApiException::class.java)
            account.idToken?.let { it ->
                auth = FirebaseAuth.getInstance()
                val credential = GoogleAuthProvider.getCredential(it, null)
                auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        val user = auth.currentUser
                        //_signInName.value = user?.displayName
                        navController.navigate(NavItem.LastReportNavItem.screenRoute)
                        //_progress.value = false
                    } else {
                        Log.e("signIn", "unSuccess signin")
                        //_progress.value = false
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("signIn", "error signin: ${e.message}")
            _logginIn.value = false
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

}