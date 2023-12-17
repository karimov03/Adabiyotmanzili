package com.example.adabiyotmanzili.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.documentfile.provider.DocumentFile
import com.example.adabiyotmanzili.Activitys.HomeActivity
import com.example.adabiyotmanzili.Activitys.ReadingBookActivity
import com.example.adabiyotmanzili.Adapters.OfflineAdapter
import com.example.adabiyotmanzili.R
import com.example.adabiyotmanzili.databinding.FragmentHomeOfflineBinding
import com.example.adabiyotmanzili.databinding.FragmentHomeOnlineBinding
import com.example.adabiyotmanzili.fragments.HomeOnlineFragment
import com.example.adabiyotmanzili.models.BooleanInternet
import com.example.adabiyotmanzili.models.OfflineFile

class HomeOfflineFragment : Fragment() {
    var doubleBackPressed = false
    private lateinit var binding: FragmentHomeOfflineBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeOfflineBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (!doubleBackPressed) {
                Toast.makeText(requireContext(), "Chiqish uchun ikki marta bosing", Toast.LENGTH_SHORT).show()
                doubleBackPressed = true
                false
            } else {
                requireActivity().finishAffinity()
                super.onResume()
            }
        }
        if (BooleanInternet.internet==false){

            val homeActivity = requireActivity() as HomeActivity
            homeActivity.UpdateOflineView()
        }
    }
}
