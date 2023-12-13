package com.example.adabiyotmanzili.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.adabiyotmanzili.Activitys.HomeActivity
import com.example.adabiyotmanzili.R
import com.example.adabiyotmanzili.databinding.ActivityHomeBinding

class UserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val navController = findNavController()
            navController.navigate(R.id.homeOnlineFragment)
            val homeActivity = requireActivity() as HomeActivity
            homeActivity.updateHomeView()
            homeActivity.updateUserView()


        }
    }

}