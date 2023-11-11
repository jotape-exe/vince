package com.company.ourfinances.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityMainBinding
import com.company.ourfinances.view.fragments.CardFragment
import com.company.ourfinances.view.fragments.GoalFragment
import com.company.ourfinances.view.fragments.HomeFragment
import com.company.ourfinances.view.fragments.InsightsFragment
import com.company.ourfinances.view.preferences.ThemePreferences
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var header: View
    private lateinit var themePreferences: ThemePreferences
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        themePreferences = ThemePreferences(this)

        listeners()

        setDrawerMenu()

        setBottomMenu()

        if (savedInstanceState == null) {
            currentFragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_bottom, currentFragment!!)
                .commit()
        } else {
            currentFragment = supportFragmentManager.findFragmentById(R.id.frame_bottom)
        }


    }


    private fun listeners() {

        binding.floatingBtn.setOnClickListener {
            startActivity(Intent(this, FinanceActivity::class.java))
        }

        header = binding.navView.getHeaderView(0)

        header.setOnClickListener {

        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_help -> {
                    sendEmail()
                }

                R.id.item_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }

            }
            drawerLayout.closeDrawer(GravityCompat.START)

            menuItem.isChecked = false
            false
        }

    }

    private fun setDrawerMenu() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayoutMain

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

        fragmentTransaction.replace(R.id.frame_bottom, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        return true
    }

    private fun setBottomMenu() {
        replaceFragment(HomeFragment())
        binding.bottomNavigationView.background = null

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_home -> replaceFragment(HomeFragment())
                R.id.item_card -> replaceFragment(CardFragment())
                R.id.item_goal -> replaceFragment(GoalFragment())
                R.id.item_insights -> replaceFragment(InsightsFragment())
                else -> true
            }
        }
    }

    private fun sendEmail(): Boolean {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("joaoxstone@gmail.com"))
        }
        startActivity(intent)
        return true
    }

}