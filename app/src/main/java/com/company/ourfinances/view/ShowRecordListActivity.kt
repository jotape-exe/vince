package com.company.ourfinances.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityShowRecordListBinding
import com.company.ourfinances.model.enums.EnumUtils
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.view.fragments.ExpenseListFragment
import com.company.ourfinances.view.fragments.RevenueListFragment
import com.company.ourfinances.view.fragments.TransferListFragment

class ShowRecordListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowRecordListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowRecordListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding = ActivityShowRecordListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when (intent.getStringExtra(getString(R.string.fragmentIdentifier))) {
            EnumUtils.getRegisterType(RegisterTypeEnum.RECEITA, this) -> replaceFragment(RevenueListFragment())
            EnumUtils.getRegisterType(RegisterTypeEnum.DESPESA, this) -> replaceFragment(ExpenseListFragment())
            EnumUtils.getRegisterType(RegisterTypeEnum.TRANSFERENCIA, this) -> replaceFragment(TransferListFragment())
        }

        binding.textTitle.text = intent.getStringExtra(getString(R.string.fragmentIdentifier))

        listener()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_list, fragment).commit()
    }

    private fun listener() {
        binding.imageBack.setOnClickListener {
            finish()
        }
    }


}