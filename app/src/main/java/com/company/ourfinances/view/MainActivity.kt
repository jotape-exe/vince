package com.company.ourfinances.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.company.ourfinances.FinanceActivity
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityMainBinding
import com.company.ourfinances.view.fragments.CardFragment
import com.company.ourfinances.view.fragments.GoalFragment
import com.company.ourfinances.view.fragments.HelpFragment
import com.company.ourfinances.view.fragments.HomeFragment
import com.company.ourfinances.view.fragments.InfoFragment
import com.company.ourfinances.view.fragments.InsightsFragment
import com.company.ourfinances.view.fragments.SettingsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        fab = findViewById(R.id.floating_btn)

        listeners()

        setDrawerMenu()

        setBottomMenu()

    }

    private fun firebaseLogout(): Boolean {
        FirebaseAuth.getInstance().signOut()
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
        return true
    }

    private fun listeners() {
        fab.setOnClickListener {
            startActivity(Intent(this, FinanceActivity::class.java))
        }
    }

    private fun setDrawerMenu() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayoutMain
        val navView: NavigationView = binding.navView

        navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )

        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()
    }

    private fun replaceFragment(fragment: Fragment): Boolean {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_bottom, fragment).commit()

        return true
    }

    private fun setBottomMenu() {
        replaceFragment(HomeFragment())
        binding.bottomNavigationView.background = null

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> replaceFragment(HomeFragment())
                R.id.item_card -> replaceFragment(CardFragment())
                R.id.item_goal -> replaceFragment(GoalFragment())
                R.id.item_insights -> replaceFragment(InsightsFragment())
                else -> true
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_settings -> replaceFragment(SettingsFragment())
            R.id.item_help -> replaceFragment(HelpFragment())
            R.id.item_info -> replaceFragment(InfoFragment())
            R.id.item_logout -> firebaseLogout()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}