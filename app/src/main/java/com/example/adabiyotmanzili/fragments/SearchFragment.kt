package com.example.adabiyotmanzili.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.example.adabiyotmanzili.Activitys.HomeActivity
import com.example.adabiyotmanzili.R

class SearchFragment : Fragment() {
    var doubleBackPressed = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }
    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val navController = findNavController()
            navController.navigate(R.id.homeOnlineFragment)
            val homeActivity = requireActivity() as HomeActivity
            homeActivity.updateHomeView()
            homeActivity.updateSearchView()


        }
    }
}