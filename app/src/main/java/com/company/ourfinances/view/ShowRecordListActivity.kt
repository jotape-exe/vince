package com.company.ourfinances.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityShowRecordListBinding

class ShowRecordListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowRecordListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowRecordListBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}