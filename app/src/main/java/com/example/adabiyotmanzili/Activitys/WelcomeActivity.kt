package com.example.adabiyotmanzili.Activitys

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.adabiyotmanzili.R
import com.example.adabiyotmanzili.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    var doubleBackPressed = false
    private val binding by lazy { ActivityWelcomeBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Status bar rangini o'zgartirish uchun
        setStatusBarColor(R.color.white)
        // Status bar'dagi iconkalarni o'zgartirish uchun
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setStatusBarIconsColor(R.color.black)
        }
        binding.btnKeyinroq.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }


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


    override fun onBackPressed() {
        if (!doubleBackPressed) {
            Toast.makeText(this, "Chiqish uchun ikki marta bosing", Toast.LENGTH_SHORT).show()
            doubleBackPressed = true
            Thread{
                Thread.sleep(2000)
                doubleBackPressed = false
            }.start()
        } else {
            finishAffinity()
            super.onBackPressed()
        }
    }
}