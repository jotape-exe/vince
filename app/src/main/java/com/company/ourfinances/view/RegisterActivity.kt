package com.company.ourfinances.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        listeners()

        val fullTextLogin = binding.textToLogin.text.toString()

        binding.textToLogin.apply {
            val coloredText = SpannableString(fullTextLogin)
            coloredText.setSpan(ForegroundColorSpan(getColor(R.color.main_dark_green)), 29, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            this.text = coloredText
        }
    }

    private fun listeners() {
        binding.buttonRegister.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.textToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

}
