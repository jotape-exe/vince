package com.company.ourfinances.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityShowRecordListBinding
import com.company.ourfinances.model.constants.DatabaseConstants
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.model.preferences.FinancePreferences
import com.company.ourfinances.view.fragments.ExpenseListFragment
import com.company.ourfinances.view.fragments.RevenueListFragment
import com.company.ourfinances.view.fragments.TransferListFragment

class ShowRecordListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowRecordListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowRecordListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val financePrefs = FinancePreferences(this)
        val selectedTitle = financePrefs.getStoredIdentifier(DatabaseConstants.PreferencesConstants.KEY_TITLE_RECORD)

        when (selectedTitle) {
            TitleEnum.DESPESA.value -> replaceFragment(ExpenseListFragment())
            TitleEnum.RECEITA.value -> replaceFragment(RevenueListFragment())
            TitleEnum.TRANSFERENCIA.value -> replaceFragment(TransferListFragment())
        }

        binding.textTitle.text = selectedTitle

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
