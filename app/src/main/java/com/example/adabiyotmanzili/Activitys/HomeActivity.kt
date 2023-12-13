package com.example.adabiyotmanzili.Activitys

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.graphics.Color
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private var booleanInternet = false
    private val connectivityReceiver = ConnectivityReceiver()
    private val connectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            booleanInternet = true
            val navController = findNavController(R.id.my_navigation_host)

            GlobalScope.launch(Dispatchers.Main) {
                if (binding.viewOffline.visibility == View.VISIBLE) {
                    binding.viewOffline.visibility = View.GONE

                    binding.viewOnline.visibility = View.VISIBLE
                    binding.viewOffline.visibility = View.GONE

                    delay(1000)
                    navController.navigate(R.id.homeOnlineFragment)
                    delay(2000)
                    binding.viewOnline.visibility = View.GONE

                } else {
                    val currentDestination = navController.currentDestination
                    val fragmentName = currentDestination?.label.toString()

                    if (fragmentName != "fragment_home_online") {

                        binding.viewOnline.visibility = View.VISIBLE
                        binding.viewOffline.visibility = View.GONE

                        delay(1000)
                        navController.navigate(R.id.homeOnlineFragment)
                        delay(2000)
                        binding.viewOnline.visibility = View.GONE
                    }

                }
            }

        }

        override fun onLost(network: Network) {
            super.onLost(network)
            booleanInternet = false

            GlobalScope.launch(Dispatchers.Main) {
                val navController = findNavController(R.id.my_navigation_host)
                val currentDestination = navController.currentDestination
                val fragmentName = currentDestination?.label.toString()

                if (fragmentName != "fragment_home_offline") {
                    binding.viewOnline.visibility = View.GONE
                    binding.viewOffline.visibility = View.VISIBLE

                }
            }
        }
    }
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    override fun onStart() {
        super.onStart()
        BooleanFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        BooleanFragment()
        setStatusBarColor(R.color.white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setStatusBarIconsColor(R.color.black)
        }
        registerReceiver(
            connectivityReceiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        }


        val navController = findNavController(R.id.my_navigation_host)

        binding.openDownloads.setOnClickListener {
            if (navController.currentDestination?.id != R.id.homeOfflineFragment || navController.currentDestination?.id != R.id.homeOnlineFragment) {
                binding.apply {
                    homeImage.setImageResource(R.drawable.ic_home)
                    homeName.setTextColor(Color.parseColor("#8E2912"))


                    bookImage.setImageResource(R.drawable.ic_book_light)
                    bookName.setTextColor(Color.parseColor("#000000"))
                    userImage.setImageResource(R.drawable.ic_user_light)
                    userName.setTextColor(Color.parseColor("#000000"))
                    searchImage.setImageResource(R.drawable.ic_search_light)
                    searchText.setTextColor(Color.parseColor("#000000"))
                }
                navController.popBackStack()
                BooleanFragment()
            }
            binding.viewOffline.visibility = View.GONE
        }

        binding.btnHome.setOnClickListener {
            if (navController.currentDestination?.id != R.id.homeOfflineFragment ) {
                binding.apply {
                    homeImage.setImageResource(R.drawable.ic_home)
                    homeName.setTextColor(Color.parseColor("#8E2912"))


                    bookImage.setImageResource(R.drawable.ic_book_light)
                    bookName.setTextColor(Color.parseColor("#000000"))
                    userImage.setImageResource(R.drawable.ic_user_light)
                    userName.setTextColor(Color.parseColor("#000000"))
                    searchImage.setImageResource(R.drawable.ic_search_light)
                    searchText.setTextColor(Color.parseColor("#000000"))
                }
                navController.popBackStack()
                BooleanFragment()
            }
        }

        binding.btnBook.setOnClickListener {
            if (navController.currentDestination?.id != R.id.bookFragment) {
                binding.apply {
                    bookImage.setImageResource(R.drawable.ic_book_dark)
                    bookName.setTextColor(Color.parseColor("#8E2912"))


                    userImage.setImageResource(R.drawable.ic_user_light)
                    userName.setTextColor(Color.parseColor("#000000"))
                    homeImage.setImageResource(R.drawable.ic_home_light)
                    homeName.setTextColor(Color.parseColor("#000000"))
                    searchImage.setImageResource(R.drawable.ic_search_light)
                    searchText.setTextColor(Color.parseColor("#000000"))
                }
                navController.popBackStack()
                navController.navigate(R.id.bookFragment)
            }
        }

        binding.btnSearch.setOnClickListener {
            if (navController.currentDestination?.id != R.id.searchFragment) {
                binding.apply {
                    searchImage.setImageResource(R.drawable.ic_search_dark)
                    searchText.setTextColor(Color.parseColor("#8E2912"))


                    userImage.setImageResource(R.drawable.ic_user_light)
                    userName.setTextColor(Color.parseColor("#000000"))
                    homeImage.setImageResource(R.drawable.ic_home_light)
                    homeName.setTextColor(Color.parseColor("#000000"))
                    bookImage.setImageResource(R.drawable.ic_book_light)
                    bookName.setTextColor(Color.parseColor("#000000"))
                }
                navController.popBackStack()
                navController.navigate(R.id.searchFragment)
            }
        }

        binding.btnUser.setOnClickListener {
            if (navController.currentDestination?.id != R.id.userFragment) {
                binding.apply {
                    userImage.setImageResource(R.drawable.ic_user_dark)
                    userName.setTextColor(Color.parseColor("#8E2912"))


                    homeImage.setImageResource(R.drawable.ic_home_light)
                    homeName.setTextColor(Color.parseColor("#000000"))
                    bookImage.setImageResource(R.drawable.ic_book_light)
                    bookName.setTextColor(Color.parseColor("#000000"))
                    searchImage.setImageResource(R.drawable.ic_search_light)
                    searchText.setTextColor(Color.parseColor("#000000"))
                }
                navController.popBackStack()
                navController.navigate(R.id.userFragment)
            }
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

    private fun BooleanFragment() {
        val navController = findNavController(R.id.my_navigation_host)
        if (booleanInternet == true) {
            navController.navigate(R.id.homeOnlineFragment)
        } else {
            navController.navigate(R.id.homeOfflineFragment)
        }
    }
    fun updateHomeView() {
        binding.homeImage.setImageResource(R.drawable.ic_home)
        binding.homeName.setTextColor(Color.parseColor("#8E2912"))
    }
    fun updateBookView() {
        binding.bookImage.setImageResource(R.drawable.ic_book_light)
        binding.bookName.setTextColor(Color.parseColor("#000000"))
    }
    fun updateSearchView() {
        binding.searchImage.setImageResource(R.drawable.ic_search_light)
        binding.searchText.setTextColor(Color.parseColor("#000000"))
    }
    fun updateUserView() {
        binding.userImage.setImageResource(R.drawable.ic_user_light)
        binding.userName.setTextColor(Color.parseColor("#000000"))
    }
    fun hideOfflineView(){
        binding.viewOffline.visibility=View.GONE
    }

}