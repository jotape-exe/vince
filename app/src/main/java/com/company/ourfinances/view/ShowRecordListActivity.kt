package com.company.ourfinances.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityShowRecordListBinding
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.view.fragments.ExpenseListFragment
import com.company.ourfinances.view.fragments.RevenueListFragment
import com.company.ourfinances.view.fragments.TransferListFragment

class ShowRecordListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowRecordListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowRecordListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when (intent.getStringExtra(getString(R.string.fragmentidentifier))) {
            TitleEnum.DESPESA.value -> replaceFragment(ExpenseListFragment())
            TitleEnum.RECEITA.value -> replaceFragment(RevenueListFragment())
            TitleEnum.TRANSFERENCIA.value -> replaceFragment(TransferListFragment())
        }

        binding.textTitle.text = intent.getStringExtra(getString(R.string.fragmentidentifier))

        listener()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_list, fragment).commit()
    }

    private fun listener(){
        binding.imageBack.setOnClickListener {
            finish()
        }
    }


}
