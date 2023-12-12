package com.example.adabiyotmanzili.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import com.example.adabiyotmanzili.Adapters.OfflineAdapter
import com.example.adabiyotmanzili.databinding.FragmentHomeOfflineBinding
import com.example.adabiyotmanzili.models.OfflineFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class HomeOfflineFragment : Fragment() {
    private val PICK_DIRECTORY_REQUEST_CODE = 999
    private lateinit var adapter: OfflineAdapter
    private val fileList = ArrayList<OfflineFile>()
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
        pickDirectory()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        adapter = OfflineAdapter(requireContext(), fileList)
        binding.rv.adapter = adapter
    }

    fun getFilesInDirectory(directoryUri: Uri) {
        GlobalScope.launch(Dispatchers.IO) {
            val directoryDocumentFile = DocumentFile.fromTreeUri(requireContext(), directoryUri)
            if (directoryDocumentFile != null && directoryDocumentFile.isDirectory) {
                for (i in directoryDocumentFile.listFiles() ?: emptyArray()) {
                    // Add the file name to the list
                    val uri = Uri.parse(i.uri.toString())
                    val documentFile = DocumentFile.fromSingleUri(requireContext(), uri)
                    val fileSize = i.length()
                    val thumbnail = getThumbnail(requireContext().contentResolver, documentFile)
                    val file = OfflineFile()
                    file.file_name = i.name
                    file.file_uri = i.uri.toString()
                    file.file_size = fileSize
                    file.thumbnail = thumbnail
                    if (i.name.toString().endsWith(".pdf", ignoreCase = true)) {
                        fileList.add(file)
                    }
                }
                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun generatePdfThumbnail(pdfUri: Uri): Bitmap? {
        return try {
            val resolver = requireContext().contentResolver
            val parcelFileDescriptor = resolver.openFileDescriptor(pdfUri, "r")
            val pdfRenderer = PdfRenderer(parcelFileDescriptor!!)
            val page = pdfRenderer.openPage(0)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()
            pdfRenderer.close()
            bitmap
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    @SuppressLint("Range")
    private fun getThumbnail(contentResolver: ContentResolver, file: DocumentFile?): Bitmap? {
        return if (file != null) {
            if (file.type == "application/pdf") {
                // Handle PDF files separately
                generatePdfThumbnail(file.uri)
            } else {
                val cursor: Cursor? = contentResolver.query(
                    MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Images.Thumbnails.DATA),
                    "${MediaStore.Images.Thumbnails.IMAGE_ID}=?",
                    arrayOf(file.uri?.lastPathSegment),
                    null
                )

                cursor?.use {
                    if (it.moveToFirst()) {
                        val thumbnailPath =
                            it.getString(it.getColumnIndex(MediaStore.Images.Thumbnails.DATA))
                        return BitmapFactory.decodeFile(thumbnailPath)
                    }
                }
                null
            }
        } else {
            null
        }
    }

    fun pickDirectory() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, PICK_DIRECTORY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_DIRECTORY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val directoryUri = data?.data
                // Handle the obtained directoryUri as needed
                if (directoryUri != null) {
                    getFilesInDirectory(directoryUri)
                }
            } else {
                throw RuntimeException("Failed to get directory URI")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}