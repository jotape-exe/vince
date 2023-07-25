package com.company.ourfinances.model.service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.company.ourfinances.model.constants.RemoteConstants
import com.company.ourfinances.view.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthManager(private val activity: AppCompatActivity) {

    private var googleSignInClient: GoogleSignInClient
    private var auth: FirebaseAuth = Firebase.auth

    init {

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(RemoteConstants.SERVER_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity.applicationContext, googleSignInOptions)

    }

    fun loginWithEmailAndPassword(email: String, password: String){
    }

    private fun openMainActivity(activity: AppCompatActivity) {
        activity.startActivity(Intent(activity.applicationContext, MainActivity::class.java))
        activity.finish()
    }

}