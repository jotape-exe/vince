package com.company.ourfinances

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.company.ourfinances.databinding.ActivityFinanceBinding
import com.company.ourfinances.view.adapters.FinanceTabAdapter
import com.google.android.material.tabs.TabLayout

class FinanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinanceBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var financeTabAdapter: FinanceTabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFinanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabLayout = binding.tabFinance
        viewPager2 = binding.viewPagerFinance
        financeTabAdapter = FinanceTabAdapter(this)

        viewPager2.adapter = financeTabAdapter

        tabLayout.addOnTabSelectedListener( object : TabLayout.OnTabSelectedListener{
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

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })

    }
}