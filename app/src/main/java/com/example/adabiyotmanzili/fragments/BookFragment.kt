package com.example.adabiyotmanzili.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.example.adabiyotmanzili.Activitys.HomeActivity
import com.example.adabiyotmanzili.Activitys.ReadingBookActivity
import com.example.adabiyotmanzili.R
import com.example.adabiyotmanzili.databinding.FragmentBookBinding
import com.example.adabiyotmanzili.models.BooleanInternet

class BookFragment : Fragment() {
    var doubleBackPressed = false
    private val binding by lazy { FragmentBookBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.btnDownload.setOnClickListener {
            val intent= Intent(requireContext(),ReadingBookActivity::class.java)
            startActivity(intent)
        }
        binding.btnSaved.setOnClickListener {
            findNavController().navigate(R.id.bookListFragment)
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val navController = findNavController()
            navController.navigate(R.id.homeOnlineFragment)
            val homeActivity = requireActivity() as HomeActivity
            homeActivity.updateHomeView()
            homeActivity.updateBookView()


        }
    }
}