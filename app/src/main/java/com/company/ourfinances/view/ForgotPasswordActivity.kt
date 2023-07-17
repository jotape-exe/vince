package com.company.ourfinances.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityForgotPasswordBinding
import com.company.ourfinances.view.assets.LoadingDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)

        setContentView(binding.root)

        loadingDialog = LoadingDialog(this)

        val bundle = intent.extras
        val values = bundle?.getString("email")

        if (values != null){
            binding.editExistingEmail.text = Editable.Factory.getInstance().newEditable(values.toString())
        }

        listeners()

    }

    private fun listeners() {
        binding.buttonSendLink.setOnClickListener {
            if (TextUtils.isEmpty(binding.editExistingEmail.text.toString())){
                binding.editExistingEmail.error = getString(R.string.user_not_empty)
            } else{
                loadingDialog.startLoadingDialog()
                val emailAddress = binding.editExistingEmail.text.toString()
                Firebase.auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            loadingDialog.dismissDialog()
                            val snack = Snackbar.make(binding.mainForgotPassword, getString(R.string.send_to_email), Snackbar.LENGTH_LONG)
                            snack.setAction(getString(R.string.open_email_app)){
                                val intent = packageManager.getLaunchIntentForPackage(getString(R.string.com_google_android_gm))
                                startActivity(intent)
                            }
                            snack.show()
                        } else{
                            Log.e("error: ", task.exception.toString())
                        }
                    }
            }
        }
    }
}