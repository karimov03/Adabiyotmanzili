package com.example.adabiyotmanzili.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import com.example.adabiyotmanzili.Adapters.OfflineAdapter
import com.example.adabiyotmanzili.databinding.FragmentHomeOfflineBinding
import com.example.adabiyotmanzili.models.OfflineFile
import java.io.IOException

class HomeOfflineFragment : Fragment() {
    private val PICK_DIRECTORY_REQUEST_CODE = 999
    private val PERMISSION_REQUEST_CODE = 999
    private lateinit var adapter: OfflineAdapter
    private var selectedVolumeUri: Uri? = null
    private var fileList = ArrayList<OfflineFile>()
    private val binding by lazy { FragmentHomeOfflineBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.btnAudioKitoblar.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Offline rejimda audio kitoblardan foydalanib bo'lmaydi",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (Build.VERSION.SDK_INT >= 33) {
            pickDirectory()
            Toast.makeText(requireContext(), "sdk>33", Toast.LENGTH_SHORT).show()

        }
        else {
            Toast.makeText(requireContext(), "sdk<33", Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    requestStoragePermission()
                } else {
                    // Fayllarni o'qish huquqi mavjud, shu joyda logika boshqa ishlarni bajarishingiz mumkin
                    loadPdfFiles()
                }
            } else {
                // Android 10 va undan avval huquqlarni tekshirish
                if (checkStoragePermission()) {
                    // Fayllarni o'qish huquqi mavjud, shu joyda logika boshqa ishlarni bajarishingiz mumkin
                    loadPdfFiles()
                } else {
                    requestStoragePermission()
                }
            }
        }





        return binding.root
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }
    private fun readPdfFiles(): ArrayList<OfflineFile> {
        val pdfFiles = mutableListOf<OfflineFile>()
        val directory = requireContext().getExternalFilesDir(null)

        if (directory != null && directory.isDirectory) {
            val files = directory.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isFile && file.extension.equals(".pdf", true)) {
                        val offlineFile = OfflineFile()
                        offlineFile.file_name = file.name
                        offlineFile.file_uri = file.absolutePath
                        offlineFile.file_size = file.length() // Sizning thumbnail olish logikangizni qo'shing
                        pdfFiles.add(offlineFile)
                    }
                }
            }
        }

        return pdfFiles as ArrayList<OfflineFile>
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Foydalanuvchidan huquq qabul qilindi, shu joyda logika boshqa ishlarni bajarishingiz mumkin
                    loadPdfFiles()
                } else {
                    // Foydalanuvchidan huquq rad etildi, shu joyda kerakli manbalarni bering yoki ilovalar orqali sozlang
                    Toast.makeText(
                        requireContext(),
                        "Faylga kirish huquqi bermadingiz, ilovalarni sozlash yoki sozlashni qabul qilish",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun loadPdfFiles() {
        val pdfFiles = readPdfFiles()

        // Misol bilan, olingan pdfFiles ro'yxatini RecyclerView yoki boshqa UI komponenti orqali ko'rsating
        for (file in pdfFiles) {
            Toast.makeText(
                requireContext(),
                "Name: ${file.file_name}, URI: ${file.file_uri}, Size: ${file.file_size} bytes",
                Toast.LENGTH_LONG
            ).show()
            Toast.makeText(requireContext(), "$pdfFiles", Toast.LENGTH_SHORT).show()
            adapter = OfflineAdapter(requireContext(), pdfFiles)
            binding.rv.adapter = adapter
        }
    }


    override fun onResume() {
        super.onResume()
        adapter = OfflineAdapter(requireContext(), fileList)
        binding.rv.adapter = adapter
        adapter.notifyDataSetChanged()
    }
    private fun updateAdapter() {
        adapter = OfflineAdapter(requireContext(), fileList)
        binding.rv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun getFilesInDirectory(directoryUri: Uri) {

        fileList.clear()
        val directoryDocumentFile = DocumentFile.fromTreeUri(requireContext(), directoryUri)

        // Check if the directory exists and is a directory
        if (directoryDocumentFile != null && directoryDocumentFile.isDirectory) {
            // Iterate through files in the directory
            for (i in directoryDocumentFile.listFiles() ?: emptyArray()) {
                // Add the file name to the list
                val uri = Uri.parse(i.uri.toString())
                val documentFile = DocumentFile.fromSingleUri(requireContext(), uri)

                // Get file size in bytes
                val fileSize = i.length()

                // Get thumbnail if available
                val thumbnail = loadPdfCover(requireContext(), i.uri)

                // Create an OfflineFile instance and add it to the list
                val file = OfflineFile()
                file.file_name = i.name
                file.thumbnail = thumbnail
                file.file_uri = i.uri.toString()

                if (i.name.toString().endsWith(".pdf") || i.name.toString().endsWith(".PDF")) {
                    fileList.add(file)
                }
            }
        }

        // Notify the adapter about the data change
        adapter.notifyDataSetChanged()

    }


    private fun setupAdapter() {
        adapter = OfflineAdapter(requireContext(), fileList)
        binding.rv.adapter = adapter
        // Notify the adapter about the data change
        adapter.notifyDataSetChanged()
    }

    // ... (other code)

    fun pickDirectory() {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            startActivityForResult(intent, PICK_DIRECTORY_REQUEST_CODE)
            // İlk jil seçildiyse, o jildin bilgilerini oku
            selectedVolumeUri?.let {
                getFilesInDirectory(it)
                setupAdapter()
            }
        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val directoryUri = data?.data
            // Handle the obtained directoryUri as needed
            if (directoryUri != null) {

                val fileNames = getFilesInDirectory(directoryUri)


            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun loadPdfCover(context: Context, pdfUri: Uri): Bitmap? {
        try {
            // PDF dosyasını aç
            val fileDescriptor = context.contentResolver.openFileDescriptor(pdfUri, "r")
            fileDescriptor?.let {
                val pdfRenderer = PdfRenderer(fileDescriptor)

                // İlk sayfayı al
                val page = pdfRenderer.openPage(0)

                // Sayfadan bir önizleme bitmap'i oluştur
                val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                // Kullanılan kaynakları serbest bırak
                page.close()
                pdfRenderer.close()
                fileDescriptor.close()

                return bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

}