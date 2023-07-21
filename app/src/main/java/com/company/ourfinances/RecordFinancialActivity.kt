package com.company.ourfinances

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.company.ourfinances.databinding.ActivityRecordFinancialBinding

class RecordFinancialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecordFinancialBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityRecordFinancialBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)

        setContentView(binding.root)
    }
}