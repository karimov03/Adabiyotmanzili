package com.example.adabiyotmanzili.activitys

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.adabiyotmanzili.R

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        // Status bar rangini o'zgartirish uchun
        setStatusBarColor(R.color.white)
        // Status bar'dagi iconkalarni o'zgartirish uchun
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setStatusBarIconsColor(R.color.black)
        }
        //
        val startThread=Thread(){
            Thread.sleep(1500)
            val intent = Intent(this,WelcomeActivity::class.java)
            startActivity(intent)
        }
        startThread.start()

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
}