package com.company.ourfinances.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.company.ourfinances.databinding.ActivityFinanceBinding
import com.company.ourfinances.view.adapters.FinanceTabAdapter
import com.company.ourfinances.view.fragments.RevenueFragment
import com.company.ourfinances.viewmodel.FinanceActivityViewModel
import com.google.android.material.tabs.TabLayout

class FinanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinanceBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var financeTabAdapter: FinanceTabAdapter
    private lateinit var viewModel: FinanceActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFinanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]

        adapters()

        listeners()

    }

    private fun adapters() {
        tabLayout = binding.tabFinance
        viewPager2 = binding.viewPagerFinance
        financeTabAdapter = FinanceTabAdapter(this)
        viewPager2.adapter = financeTabAdapter
    }

    private fun listeners() {

        tabLayout.addOnTabSelectedListener( object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                    //Depuração
                    Log.i("info:","Selecionado -> ${viewPager2.currentItem}")
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })

        binding.fabSaveRegister.setOnClickListener {
            val currentPosition = viewPager2.currentItem
            val currentFragmentTag = "f$currentPosition"
            val currentFragment = supportFragmentManager.findFragmentByTag(currentFragmentTag)

            when (currentPosition) {
                0 -> {
                    val revenueFragment = currentFragment as? RevenueFragment
                    revenueFragment?.let {
                        it.onFabClicked()
                    }

                    viewModel.save()

                }
                1 -> {

                }
                2 -> {

                }
            }
        }
    }

    private fun observer(){

    }
}