package com.company.ourfinances.view.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityWelcomeBinding
import com.company.ourfinances.view.MainActivity
import com.company.ourfinances.view.preferences.StartPreferences

class Welcome : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var preferences: StartPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        preferences = StartPreferences(this)

        setContentView(binding.root)

        binding.buttonStart.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            preferences.setState(1)
        }

    }

    override fun onStart() {
        super.onStart()
        if (preferences.getState() == 1) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}