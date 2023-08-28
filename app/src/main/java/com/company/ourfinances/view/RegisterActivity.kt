package com.company.ourfinances.view

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityRegisterBinding
import com.company.ourfinances.view.utils.LoadingDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        listeners()

        auth = Firebase.auth

        loadingDialog = LoadingDialog(this)

        binding.textToLogin.run {
            val coloredText = SpannableString(binding.textToLogin.text.toString())
            coloredText.setSpan(
                ForegroundColorSpan(getColor(R.color.variant_green_3)),
                29,
                34,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            text = coloredText
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::loadingDialog.isInitialized) {
            loadingDialog.dismissDialog()
        }
    }

    private fun listeners() {
        binding.buttonRegister.setOnClickListener {
            val email = binding.editEmailRegister.text
            val password = binding.editPasswordRegister.text

            if (TextUtils.isEmpty(email)) {
                binding.editEmailRegister.error = getString(R.string.user_not_empty)
            } else if (TextUtils.isEmpty(password)) {
                binding.editPasswordRegister.error = getString(R.string.password_not_empty)
            } else {
                loadingDialog.startLoadingDialog()
                registerWithEmailAndPassword(email.toString(), password.toString())
            }
        }

        binding.textToLogin.setOnClickListener {
            finish()
        }
    }

    private fun registerWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loadingDialog.dismissDialog()
                    openMainActivity(Intent(this, MainActivity::class.java))
                } else {
                    task.exception?.let { exception ->
                        when (exception) {
                            is FirebaseAuthUserCollisionException -> {
                                setErrorMessage(binding.editEmailRegister, getString(R.string.email_has_been_registered))
                            }

                            is FirebaseAuthWeakPasswordException -> {
                                setErrorMessage(binding.editPasswordRegister, getString(R.string.Password_had_more_than_6_characters))
                            }

                            else -> {
                                Log.e("error: ", exception.toString())
                                showSnackbarMessage(binding.registerMain, getString(R.string.error_when_registering))
                            }

                        }
                    }

                }
            }
    }

    private fun openMainActivity(intent: Intent) {
        startActivity(intent)
        finish()
    }

    private fun setErrorMessage(textView: TextView, message: String) {
        textView.error = message
        loadingDialog.dismissDialog()
    }

    private fun showSnackbarMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        loadingDialog.dismissDialog()
    }


}