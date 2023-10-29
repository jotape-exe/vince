package com.company.ourfinances.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
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
import com.company.ourfinances.view.auth.LoginActivity
import com.company.ourfinances.view.fragments.CardFragment
import com.company.ourfinances.view.fragments.GoalFragment
import com.company.ourfinances.view.fragments.HomeFragment
import com.company.ourfinances.view.fragments.InsightsFragment
import com.company.ourfinances.view.utils.CurrencyObject
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    /*private val viewModel: CurrencyViewModel = CurrencyViewModel()

    //Notification?
    private val updateInterval: Long = 600000 //10 minutos
    private var job: Job? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listeners()

        setDrawerMenu()

        setBottomMenu()

        //Notification?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "running_channel"
            val channelName = "channel_1"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        //Notification?
        /*val currencyList: ArrayList<String> = arrayListOf("", "", "")

        job = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                viewModel.getLastCurrencyData()
                viewModel.currencyData.observe(this@MainActivity){

                    it.forEach {
                        currencyList.add(0, calculateCurrency(it.usdBRL.highValue, it.usdBRL.lowValue))
                        currencyList.add(1,calculateCurrency(it.btcBRL.highValue, it.btcBRL.lowValue))
                        currencyList.add(2, calculateCurrency(it.eurBRL.highValue, it.eurBRL.lowValue))
                    }

                    startNotification(currencyList)
                }
                delay(updateInterval)
            }
        }*/

        val objects = arrayListOf(
            CurrencyObject("R$ 1", R.drawable.brazil_flag),
            CurrencyObject("€ 5", R.drawable.europe)
        )


    }

    /*private fun startNotification(currencyList: ArrayList<String>) {
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(this, "running_channel")
            .setPriority(NotificationManager.IMPORTANCE_MAX)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Cotação diária")


        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText("DOLAR: R$ ${currencyList[0]}\nBITCOIN: R$ ${currencyList[1]}\nEURO: R$ ${currencyList[2]}")
            .setSummaryText("Cotação diária")

        notification.setStyle(bigTextStyle)


        val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(1, notification.build())
    }*/

    private fun firebaseLogout(): Boolean {
        FirebaseAuth.getInstance().signOut()
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
        return true
    }

    private fun listeners() {

        binding.floatingBtn.setOnClickListener {
            startActivity(Intent(this, FinanceActivity::class.java))
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

        /*binding.imageCurrency.setOnClickListener {
            //Visualização da cotação(Notification? Spinner? Popup?)
        }*/
    }

    /*private fun calculateCurrency(highValue: String, lowValue: String): String{
       return String.format("%.2f", (highValue.toDouble() + lowValue.toDouble()) / 2)
    }*/

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