package com.company.ourfinances.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityMainBinding
import com.company.ourfinances.model.remote.RetrofitClient
import com.company.ourfinances.model.repository.CurrencyRepository
import com.company.ourfinances.view.fragments.CardFragment
import com.company.ourfinances.view.fragments.GoalFragment
import com.company.ourfinances.view.fragments.HomeFragment
import com.company.ourfinances.view.fragments.InsightsFragment
import com.company.ourfinances.viewmodel.CurrencyViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    //DEBUG
    private val viewModel: CurrencyViewModel = CurrencyViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.floatingBtn.setOnClickListener {
            startActivity(Intent(this, FinanceActivity::class.java))
            viewModel.getLastCurrencyData()
            viewModel.currencyData.observe(this){
                it.forEach {
                    println(it)
                }
            }
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_help -> {
                    sendEmail()
                }
                R.id.item_info -> {
                    AlertDialog.Builder(this)
                        .setTitle("Informação")
                        .setMessage("Vince®\nDesenvolvedor: João Pedro")
                        .setPositiveButton("OK", null)
                        .create()
                        .show()
                }
                R.id.item_logout -> firebaseLogout()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
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
        fragmentTransaction.replace(R.id.frame_bottom, fragment).commit()

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