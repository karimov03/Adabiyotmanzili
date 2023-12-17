package com.example.adabiyotmanzili.Activitys

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.DocumentsContract
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adabiyotmanzili.Adapters.OfflineAdapter
import com.example.adabiyotmanzili.R
import com.example.adabiyotmanzili.databinding.ActivityReadingBookBinding
import com.example.adabiyotmanzili.models.OfflineFile
import com.example.adabiyotmanzili.models.ReadingBook
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle

class ReadingBookActivity : AppCompatActivity() {
    private val GET_BOOKS_FOLDER_NAME = "GetBooks"
    private val PICK_DIRECTORY_REQUEST_CODE = 999
    private var click_full=false
    private lateinit var gestureDetector: GestureDetector
    private val binding by lazy { ActivityReadingBookBinding.inflate(layoutInflater) }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
            pickDirectory()
        gestureDetector = GestureDetector(this, SwipeGestureListener())
        Toast.makeText(this@ReadingBookActivity, "Download/GetBooks papkasidan foydalanishga ruxsat bering", Toast.LENGTH_LONG).show()
        // Status bar rangini o'zgartirish uchun
        setStatusBarColor(R.color.white)
        // Status bar'dagi iconkalarni o'zgartirish uchun
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setStatusBarIconsColor(R.color.black)
        }
        binding.textAll.setOnClickListener {
            click_full=true
            binding.rv.setPadding(0, 0, 0, 150)
                binding.textAll.visibility=View.GONE
                binding.textOrtga.visibility=View.VISIBLE
                binding.linearLayout2.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                binding.rv.layoutManager = GridLayoutManager(this, 3)

        }
        binding.textOrtga.setOnClickListener {
            click_full=false
            binding.rv.setPadding(0, 0, 0, 0)
                binding.textAll.visibility=View.VISIBLE
                binding.textOrtga.visibility=View.GONE
                binding.linearLayout2.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                binding.rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.btnFullScreen.setOnClickListener {
            binding.linearLayout2.visibility= View.GONE
            binding.btnFullScreen.visibility=View.GONE

        }
        binding.btnSmallScreen.setOnClickListener {
            binding.btnFullScreen.visibility=View.VISIBLE
            binding.linearLayout2.visibility= View.VISIBLE
        }
        binding.btnBack.setOnClickListener {
            finish()
        }

    }

    fun loadBitmapFromFile(fileUri: String): Bitmap? {
        val fileDocumentFile = DocumentFile.fromSingleUri(this@ReadingBookActivity, Uri.parse(fileUri))

        // Check if the DocumentFile is not null and represents a file
        if (fileDocumentFile != null && fileDocumentFile.isFile) {
            // Read the file content into a byte array
            val inputStream = contentResolver.openInputStream(fileDocumentFile.uri)
            val byteArray = inputStream?.readBytes()
            inputStream?.close()

            // Convert the byte array to a Bitmap
            if (byteArray != null) {
                return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            }
        }

        return null
    }
    fun getFilesInDirectory(directoryUri: Uri) {
        val fileList = ArrayList<OfflineFile>()

        var fileThumbnail: Bitmap?
        val directoryDocumentFile = DocumentFile.fromTreeUri(this@ReadingBookActivity, directoryUri)
        if (directoryDocumentFile != null && directoryDocumentFile.isDirectory) {
            for (file in directoryDocumentFile.listFiles() ?: emptyArray()) {
                // Add the file name to the list

                val fileUriString = file.uri?.toString()

                // Check if fileUriString is not null before using it
                if (fileUriString != null) {
                    val fileThumbnail: Bitmap? = loadBitmapFromFile(fileUriString)

                    if (fileThumbnail == null) {
                        Log.e("ReadingBookActivity", "File thumbnail is null for ${file.name}")
                    }

                    val offlineFile = OfflineFile(
                        file.name.toString(),
                        file.length().toString(),
                        fileUriString,
                        fileThumbnail ?: getDefaultBitmap() 
                    )



                    if (file.name!!.endsWith(".pdf") || file.name!!.endsWith(".PDF")) {
                        if (file.name.toString().startsWith("1702")) {
                            // Do something if needed
                        } else {
                            fileList.add(offlineFile)
                        }
                    }
                } else {
                    Log.e("ReadingBookActivity", "File URI is null for ${file.name}")
                }
            }

            if (fileList.size==0){
                Toast.makeText(this, "Fayllar mavjud emas", Toast.LENGTH_SHORT).show()
            }
                val fileAdapter = OfflineAdapter(this@ReadingBookActivity,fileList,object :OfflineAdapter.RvAction{
                    override fun OnRootClick(list: List<OfflineFile>, position: Int) {
                        binding.btnFullScreen.visibility=View.VISIBLE
                        val book = list[position]
                        if (click_full==true){
                            binding.textOrtga.performClick()
                        }
                        binding.pdfView.fromUri(book.file_uri.toUri()).swipeHorizontal(false)
                            .scrollHandle(DefaultScrollHandle(this@ReadingBookActivity))
                            .enableSwipe(true).onPageChange(onPageChangeListener).load()
                    }
                })
                val recyclerView: RecyclerView = findViewById(R.id.rv)
                recyclerView.adapter = fileAdapter


        }
        else{
            Toast.makeText(this, "Fayllar mavjud emas", Toast.LENGTH_SHORT).show()
        }


    }

    private fun getDefaultBitmap(): Bitmap {
        return BitmapFactory.decodeResource(resources, R.drawable.image_book)
    }

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageChanged(page: Int, pageCount: Int) {
            PageUpdate(page, pageCount)
        }
    }

    private fun PageUpdate(page: Int, pageCount: Int) {
        // page ni kuzatish
        binding.page.text = "Page ${page + 1} of $pageCount"
//        binding.btnNext.setOnClickListener {
//            binding.pdfView.jumpTo(page+1)
//            PageUpdate(page+1,pageCount)
//        }
//        binding.btnBack.setOnClickListener {
//            binding.pdfView.jumpTo(page-1)
//            PageUpdate(page-1,pageCount)
//
//        }
    }

    inner class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffX = e2?.x!! - e1?.x!!

            if (Math.abs(diffX) > SWIPE_THRESHOLD &&
                Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD
            ) {
                if (diffX > 0) {
//                    // o'nga
//                    binding.btnNext.performClick()
                } else {
//                    //chapga
//                    binding.btnBack.performClick()
                }
                return true
            }
            return false
        }
    }
    private fun pickDirectory() {
        val initialUri = Uri.parse("file://${Environment.getExternalStorageDirectory().absolutePath}/Documents/$GET_BOOKS_FOLDER_NAME")
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialUri)
        startActivityForResult(intent, PICK_DIRECTORY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_DIRECTORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val selectedDirectory = uri
                getFilesInDirectory(selectedDirectory)
            }
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


}