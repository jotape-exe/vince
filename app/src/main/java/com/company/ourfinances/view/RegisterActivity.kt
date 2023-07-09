package com.company.ourfinances.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityRegisterBinding
import com.company.ourfinances.view.assets.LoadingDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
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

        val fullTextLogin = binding.textToLogin.text.toString()

        binding.textToLogin.apply {
            val coloredText = SpannableString(fullTextLogin)
            coloredText.setSpan(
                ForegroundColorSpan(getColor(R.color.main_dark_green)),
                29,
                34,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            this.text = coloredText
        }
    }

    private fun listeners() {
        binding.buttonRegister.setOnClickListener {
            val email = binding.editEmailRegister.text
            val password = binding.editPasswordRegister.text
            val name = binding.editNameRegister.text
            if (TextUtils.isEmpty(name)) {
                binding.editNameRegister.error = getString(R.string.name_not_empty)
            } else if (TextUtils.isEmpty(email)) {
                binding.editEmailRegister.error = getString(R.string.user_not_empty)
            } else if (TextUtils.isEmpty(password)) {
                binding.editPasswordRegister.error = getString(R.string.password_not_empty)
            } else {
                loadingDialog.startLoadingDialog()
                registerWithEmailAndPassword(email.toString(), password.toString())
            }
        }

        binding.textToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun registerWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loadingDialog.dismissDialog()
                    openMainActivity()
                } else {
                    Snackbar.make(
                        binding.registerMain,
                        "Email já foi cadastrado!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    loadingDialog.dismissDialog()
                }
            }
    }

    private fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
