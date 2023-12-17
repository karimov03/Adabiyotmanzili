package com.example.adabiyotmanzili.fragments

import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adabiyotmanzili.Activitys.HomeActivity
import com.example.adabiyotmanzili.Activitys.WelcomeActivity
import com.example.adabiyotmanzili.R
import com.example.adabiyotmanzili.databinding.FragmentHomeOnlineBinding
import com.example.adabiyotmanzili.models.BooleanInternet
import com.example.adabiyotmanzili.models.ConnectivityReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeOnlineFragment : Fragment() {
    var doubleBackPressed = false
    private val binding by lazy { FragmentHomeOnlineBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.tvRecomendationAll.setOnClickListener {
            if (binding.tvRecomendationAll.text=="Ortga"){
                binding.tvRecomendationAll.text="Hammasi"
                binding.layoutRecomendation.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                binding.rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            }
            else{
            binding.tvRecomendationAll.text="Ortga"
            binding.layoutRecomendation.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            binding.rv.layoutManager = GridLayoutManager(requireContext(), 3)
            }
        }
        binding.tvNewAll.setOnClickListener {
            if (binding.tvRecomendationAll.text=="Ortga"){
                binding.tvRecomendationAll.text="Hammasi"
                binding.layoutNewAll.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                binding.rvYangi.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            }
            else{
            binding.tvRecomendationAll.text="Ortga"
            binding.layoutNewAll.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            binding.rvYangi.layoutManager = GridLayoutManager(requireContext(), 3)
            }
        }

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

