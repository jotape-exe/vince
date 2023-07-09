package com.company.ourfinances.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider

import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityLoginBinding
import com.company.ourfinances.view.assets.LoadingDialog
import com.company.ourfinances.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth;
    private lateinit var loadingDialog: LoadingDialog

    private val SERVER_CLIENT_ID = "688509858493-ih7seb3jb90qp0916qhgp12vcr264nrg.apps.googleusercontent.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loadingDialog = LoadingDialog(this)

        setContentView(binding.root)

        listeners()

        auth = Firebase.auth

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(SERVER_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(applicationContext, googleSignInOptions)

        val fullTextRegister = binding.textRegister.text.toString()

        binding.textRegister.apply {
            val coloredText = SpannableString(fullTextRegister)
            coloredText.setSpan(
                ForegroundColorSpan(getColor(R.color.main_dark_green)),
                25,
                36,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            this.text = coloredText
        }

        observer()

    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        currentUser?.let { openMainActivity() }
    }

    private fun listeners() {
        binding.textRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener {
            loadingDialog.startLoadingDialog()
            val email = binding.editEmailLogin.text
            val password = binding.editPasswordLogin.text
            if(TextUtils.isEmpty(email)){
                binding.editEmailLogin.error = "Usuário não pode ser vazio!"
                loadingDialog.dismissDialog()
            } else if(TextUtils.isEmpty(password)){
                binding.editPasswordLogin.error = "Senha não pode ser vazia!"
                loadingDialog.dismissDialog()
            } else {
                loginWithEmailAndPassword(email.toString(), password.toString())
            }
        }

        binding.googleButton.setOnClickListener {
            signIn()
        }

    }

    private fun signIn() {
        val intent = googleSignInClient.signInIntent
        startActivityWithGoogle.launch(intent)
    }

    var startActivityWithGoogle = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult ->

        if (result.resultCode == RESULT_OK){
            val intent = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)

            try {
                val account = task.getResult(ApiException::class.java)
                loginWithGoogle(account.idToken!!)
            } catch (e: ApiException){

            }
        }
    }

    private fun loginWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            task: Task<AuthResult> ->
            if (task.isSuccessful){
                openMainActivity()
            }

        }
    }

    private fun loginWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(
            email,
            password
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCustomToken:success")
                    val user = auth.currentUser
                    openMainActivity()
                } else {
                    Log.w(TAG, "signInWithCustomToken:failure", task.exception)
                    Snackbar.make(
                        binding.loginMain,
                        "Usuário ou senha inválidos!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    loadingDialog.dismissDialog()
                }
            }
    }

    private fun openMainActivity() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    private fun observer() {

    }

}