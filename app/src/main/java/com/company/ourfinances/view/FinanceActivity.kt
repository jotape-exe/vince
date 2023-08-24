package com.company.ourfinances.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityFinanceBinding
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.view.adapters.FinanceTabAdapter
import com.company.ourfinances.view.fragments.ExpenseFragment
import com.company.ourfinances.view.fragments.RevenueFragment
import com.company.ourfinances.view.fragments.TransferFragment
import com.company.ourfinances.viewmodel.FinanceActivityViewModel
import com.google.android.material.tabs.TabLayout

class FinanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinanceBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var financeTabAdapter: FinanceTabAdapter
    private lateinit var viewModel: FinanceActivityViewModel

    //Posição de cada fragment no TabLayout
    private val REVENUE_POSITION = 0;
    private val EXPENSE_POSITION = 1;
    private val TRANSFER_POSITION = 2;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFinanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]

        adapters()

        listeners()

        when(intent.extras?.getString(getString(R.string.fragmentIdentifier))){
            TitleEnum.RECEITA.value -> {
                viewPager2.currentItem = REVENUE_POSITION
            }
            TitleEnum.DESPESA.value -> {
                viewPager2.currentItem = EXPENSE_POSITION
            }
            TitleEnum.TRANSFERENCIA.value -> {
                viewPager2.currentItem = TRANSFER_POSITION
            }
        }


    }


    private fun adapters() {
        tabLayout = binding.tabFinance
        viewPager2 = binding.viewPagerFinance
        financeTabAdapter = FinanceTabAdapter(this)
        viewPager2.adapter = financeTabAdapter
    }

    private fun listeners() {

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })

        binding.fabSaveRegister.setOnClickListener {
            currentFragmentAtTab(viewPager2.currentItem)
        }
    }

    private fun currentFragmentAtTab(currentPosition: Int) {

        val currentFragmentTag = "f$currentPosition"
        val currentFragment: Fragment? = supportFragmentManager.findFragmentByTag(currentFragmentTag)

        when (currentPosition) {
            REVENUE_POSITION -> {
                val revenueFragment = currentFragment as RevenueFragment
                revenueFragment.doSave()
            }

            EXPENSE_POSITION -> {
                val expenseFragment = currentFragment as ExpenseFragment
                expenseFragment.doSave()
            }

            TRANSFER_POSITION -> {
                val transferFragment = currentFragment as TransferFragment
                transferFragment.doSave()
            }
        }
    }


}