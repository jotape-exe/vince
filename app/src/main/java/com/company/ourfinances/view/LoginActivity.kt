package com.company.ourfinances.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider

import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityLoginBinding
import com.company.ourfinances.view.assets.LoadingDialog
import com.company.ourfinances.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth;
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loadingDialog = LoadingDialog(this)

        setContentView(binding.root)

        listeners()

        auth = Firebase.auth

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
            val email = binding.editEmailLogin.text.toString()
            val password = binding.editPasswordLogin.text.toString()
            try {
                loginWithEmailAndPassword(email, password)
            } catch (ex: Exception) {
                changeEditTextOutlineColor(
                    binding.editEmailLogin,
                    binding.editPasswordLogin,
                    colorId = R.color.red
                )
                Snackbar.make(
                    binding.loginMain,
                    "Preencha os campos corretamente!",
                    Snackbar.LENGTH_SHORT
                ).show()
                loadingDialog.dismissDialog()
            }
        }

        binding.editEmailLogin.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    changeEditTextOutlineColor(binding.editEmailLogin, colorId = R.color.black)
                }
            }

        binding.editPasswordLogin.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    changeEditTextOutlineColor(binding.editPasswordLogin, colorId = R.color.black)
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

    private fun changeEditTextOutlineColor(vararg editTexts: EditText, colorId: Int) {
        val color = ContextCompat.getColor(this, colorId)
        for (editText in editTexts) {
            val outlineDrawable: Drawable? = editText.background
            outlineDrawable?.setTint(color)
        }
    }

    private fun observer() {

    }

}