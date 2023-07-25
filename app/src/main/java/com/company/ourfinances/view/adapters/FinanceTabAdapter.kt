package com.company.ourfinances.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.company.ourfinances.view.fragments.ExpenseFragment
import com.company.ourfinances.view.fragments.RevenueFragment
import com.company.ourfinances.view.fragments.TransferFragment


class FinanceTabAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RevenueFragment()
            1 -> ExpenseFragment()
            else -> TransferFragment()
        }
    }


}