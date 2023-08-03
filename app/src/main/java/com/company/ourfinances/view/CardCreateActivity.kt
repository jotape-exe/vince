package com.company.ourfinances.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.company.ourfinances.databinding.ActivityCardCreateBinding

class CardCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCardCreateBinding.inflate(layoutInflater)

        listener()

        setContentView(binding.root)
    }

    private fun listener() {
        binding.imageClose.setOnClickListener {
            finish()
        }
    }
}