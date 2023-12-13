package com.example.adabiyotmanzili.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.example.adabiyotmanzili.Activitys.HomeActivity
import com.example.adabiyotmanzili.Activitys.WelcomeActivity
import com.example.adabiyotmanzili.Adapters.OfflineAdapter
import com.example.adabiyotmanzili.R
import com.example.adabiyotmanzili.databinding.FragmentHomeOfflineBinding
import com.example.adabiyotmanzili.models.OfflineFile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class HomeOfflineFragment : Fragment() {

    private val PICK_DIRECTORY_REQUEST_CODE = 999
    var doubleBackPressed = false

    private lateinit var adapter: OfflineAdapter
    private var fileList = ArrayList<OfflineFile>()
    private val binding by lazy { FragmentHomeOfflineBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeActivity = requireActivity() as HomeActivity
        homeActivity.updateHomeView()
        homeActivity.hideOfflineView()
        binding.btnAudioKitoblar.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Offline rejimda audio kitoblardan foydalanib bo'lmaydi",
                Toast.LENGTH_SHORT
            ).show()
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val fileSet = sharedPreferences.getStringSet("fileList", emptySet())
        fileList = (fileSet?.map { Gson().fromJson(it, OfflineFile::class.java) }
            ?: emptyList()) as ArrayList<OfflineFile>
        adapter = OfflineAdapter(requireContext(), fileList)
        if (fileList.isNullOrEmpty()) {
            pickDirectory()
        } else {
            adapter.notifyDataSetChanged()
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val fileSet = sharedPreferences.getStringSet("fileList", emptySet())
        fileList = (fileSet?.map { Gson().fromJson(it, OfflineFile::class.java) }
            ?: emptyList()) as ArrayList<OfflineFile>
        adapter = OfflineAdapter(requireContext(), fileList)
        binding.rv.adapter = adapter


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (!doubleBackPressed) {
                Toast.makeText(
                    requireContext(),
                    "Chiqish uchun ikki marta bosing",
                    Toast.LENGTH_SHORT
                ).show()
                doubleBackPressed = true
                false
            } else {
                requireActivity().finishAffinity()
                super.onResume()
            }
        }
    }

    fun getFilesInDirectory(directoryUri: Uri) {
        GlobalScope.launch(Dispatchers.IO) {
            val directoryDocumentFile = DocumentFile.fromTreeUri(requireContext(), directoryUri)
            if (directoryDocumentFile != null && directoryDocumentFile.isDirectory) {
                for (i in directoryDocumentFile.listFiles() ?: emptyArray()) {
                    val uri = Uri.parse(i.uri.toString())
                    val fileSize = i.length()
                    val file = OfflineFile()
                    file.file_name = i.name
                    file.file_uri = i.uri.toString()
                    file.file_size = fileSize
                    if (i.name.toString().endsWith(".pdf", ignoreCase = true)) {
                        fileList.add(file)
                    }
                }


                withContext(Dispatchers.Main) {
                    val sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(requireContext())
                    val editor = sharedPreferences.edit()
                    val fileSet = fileList.map { Gson().toJson(it) }.toSet()
                    editor.putStringSet("fileList", fileSet)
                    editor.apply()

                    adapter.notifyDataSetChanged()
                }
            }
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
