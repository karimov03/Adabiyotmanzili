package com.example.adabiyotmanzili.activitys

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.example.adabiyotmanzili.R
import com.example.adabiyotmanzili.databinding.ActivityHomeBinding
import com.example.adabiyotmanzili.models.ConnectivityReceiver

class HomeActivity : AppCompatActivity() {
    private var booleanInternet=false

    private val connectivityReceiver = ConnectivityReceiver()
    private val connectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {


        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            booleanInternet=true
            Toast.makeText(this@HomeActivity, "Yoon", Toast.LENGTH_SHORT).show()


        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Toast.makeText(this@HomeActivity, "O'ooch", Toast.LENGTH_SHORT).show()
            booleanInternet=false




        }
    }
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        BooleanFragment()
        val findNavController=findNavController(R.id.my_navigation_host)


        // Status bar rangini o'zgartirish uchun
        setStatusBarColor(R.color.white)
        // Status bar dagi iconkalarni o'zgartirish uchun
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setStatusBarIconsColor(R.color.black)
        }

        // internet ga ulanib qolgan holda tekshirish
        registerReceiver(
            connectivityReceiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        }

        // Dasturning bosh qismlar
        binding.btnHome.setOnClickListener {
            BooleanFragment()
        }


    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
    }
    private fun setStatusBarColor(colorResId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, colorResId)
        }
    }
    private fun setStatusBarIconsColor(colorResId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = window
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
    private fun BooleanFragment(){
        val findNavController=findNavController(R.id.my_navigation_host)
        if (booleanInternet){
            findNavController.navigate(R.id.homeOnlineFragment)
        }
        else{
            findNavController.navigate(R.id.homeOfflineFragment)
        }
        Toast.makeText(this@HomeActivity, "$booleanInternet", Toast.LENGTH_SHORT).show()
    }


}