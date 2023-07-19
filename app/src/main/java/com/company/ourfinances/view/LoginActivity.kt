package com.company.ourfinances.view

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityLoginBinding
import com.company.ourfinances.model.constants.RemoteConstants
import com.company.ourfinances.view.assets.LoadingDialog
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

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(this)

        setContentView(binding.root)

        listeners()

        auth = Firebase.auth

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(RemoteConstants.SERVER_CLIENT_ID)
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
            val email = binding.editEmailLogin.text
            val password = binding.editPasswordLogin.text
            if (TextUtils.isEmpty(email)) {
                binding.editEmailLogin.error = getString(R.string.user_not_empty)
            } else if (TextUtils.isEmpty(password)) {
                binding.editPasswordLogin.error = getString(R.string.password_not_empty)
            } else {
                loadingDialog.startLoadingDialog()
                loginWithEmailAndPassword(email.toString(), password.toString())
            }
        }

        binding.googleButton.setOnClickListener {
            signIn()
        }

        binding.textForgotPassword.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("email", binding.editEmailLogin.text.toString())
            startActivity(Intent(this, ForgotPasswordActivity::class.java).putExtras(bundle))
        }

    }

    //Create service for login features
    private fun signIn() {
        val intent = googleSignInClient.signInIntent
        startActivityWithGoogle.launch(intent)
    }

    private var startActivityWithGoogle =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(intent)

                try {
                    val account = task.getResult(ApiException::class.java)
                    loginWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Log.e("error: ",e.toString())
                }
            }
        }

    private fun loginWithGoogle(token: String) {
        loadingDialog.startLoadingDialog()
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful) {
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
                    openMainActivity()
                } else {
                    Snackbar.make(
                        binding.loginMain,
                        getString(R.string.invalid_user_or_password),
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