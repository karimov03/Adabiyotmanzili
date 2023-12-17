package com.example.adabiyotmanzili.Activitys

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.adabiyotmanzili.R
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat

class StartActivity : AppCompatActivity() {

    private val PREF_NAME = "MyPreferences"
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
        // "GetBooks" papkasini toping yoki yarating
        getOrCreateGetBooksFolder()

        //

        val startThread=Thread(){
            Thread.sleep(1500)
            val intent = Intent(this@StartActivity,WelcomeActivity::class.java)
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


    private fun getOrCreateGetBooksFolder() {
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val getBooksFolder = File(downloadsDirectory, "GetBooks")

        if (!getBooksFolder.exists()) {
            // "GetBooks" papkasi mavjud emasligini tekshirish
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                createGetBooksFolderQ(getBooksFolder)
            } else {
                getBooksFolder.mkdirs()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createGetBooksFolderQ(getBooksFolder: File) {
        val resolver: ContentResolver = contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/GetBooks")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }

        // "GetBooks" papkasi qaysi manzilga yaratilganligini tekshirish
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            resolver.openOutputStream(uri)?.use { outputStream ->
                // Siz kerak bo'lgan bo'limga ma'lumotlar yozishingiz mumkin
            }

            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
        } else {
            // Papka yaratilmasa qandaydir xatolikni boshqarish
            runOnUiThread {
                Toast.makeText(
                    this,
                    "'GetBooks' papkasini yaratib bo'lmadi",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}