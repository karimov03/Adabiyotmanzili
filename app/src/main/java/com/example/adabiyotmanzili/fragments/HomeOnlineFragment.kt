package com.example.adabiyotmanzili.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.navigation.findNavController
import com.example.adabiyotmanzili.Activitys.WelcomeActivity
import com.example.adabiyotmanzili.R
import com.example.adabiyotmanzili.databinding.FragmentHomeOnlineBinding

class HomeOnlineFragment : Fragment() {
    var doubleBackPressed = false
    private val binding by lazy { FragmentHomeOnlineBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



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
    }

}

