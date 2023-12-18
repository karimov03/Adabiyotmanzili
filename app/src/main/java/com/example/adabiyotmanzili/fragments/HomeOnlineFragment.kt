package com.example.adabiyotmanzili.fragments

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.adabiyotmanzili.Adapters.OnlineAdapter
import com.example.adabiyotmanzili.ApiServis.ApiClient
import com.example.adabiyotmanzili.Dialogs.ProgressDialogFragment
import com.example.adabiyotmanzili.R
import com.example.adabiyotmanzili.databinding.FragmentHomeOnlineBinding
import com.example.adabiyotmanzili.models.BookData
import com.example.adabiyotmanzili.models.Books
import com.example.adabiyotmanzili.models.BooleanInternet
import com.example.adabiyotmanzili.models.ConnectivityReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeOnlineFragment : Fragment() {
    var doubleBackPressed = false
    private val binding by lazy { FragmentHomeOnlineBinding.inflate(layoutInflater) }
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val progressDialogFragment = ProgressDialogFragment.newInstance()
        progressDialogFragment.isCancelable=false
        progressDialogFragment.show(requireFragmentManager(), "ProgressDialogFragment")

        try {
            val apiService = ApiClient.apiService
            val call = apiService.getBooks()
            call.enqueue(object : Callback<Books> {
                override fun onResponse(call: Call<Books>, response: Response<Books>) {
                    if (response.isSuccessful) {
                        val booksResponse = response.body()

                        booksResponse?.let {
                            if (it.results.isNotEmpty()) {
                                val repeatedList = it.results.flatMap { book -> List(8) { book } }
                                val randomList = repeatedList.shuffled().take(10)

                                try {
                                    val adapter1 = OnlineAdapter(requireContext(), randomList)
                                    binding.rv.adapter = adapter1

                                    val refreshList = repeatedList.asReversed()
                                    val adapter2 = OnlineAdapter(requireContext(), refreshList)
                                    binding.rvYangi.adapter = adapter2
                                    progressDialogFragment.dismiss()
                                }
                                catch (e:Exception){
                                        progressDialogFragment.dismiss()
                                }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Server returned an empty list",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressDialogFragment.dismiss()
                            }
                        } ?: run {
                            Toast.makeText(
                                requireContext(),
                                "Server response is null",
                                Toast.LENGTH_SHORT
                            ).show()
                            progressDialogFragment.dismiss()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Server error: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialogFragment.dismiss()
                    }
                }

                override fun onFailure(call: Call<Books>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Internet connection issue: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                    progressDialogFragment.dismiss()
                }
            })
        }
        catch (e:Exception){

        }



















        binding.tvRecomendationAll.setOnClickListener {
            if (binding.tvRecomendationAll.text=="Ortga"){
                binding.tvRecomendationAll.text="Hammasi"
                binding.layoutRecomendation.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                binding.rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            }
            else{
            binding.tvRecomendationAll.text="Ortga"
                binding.layoutNewAll.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            binding.layoutRecomendation.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            binding.rv.layoutManager = GridLayoutManager(requireContext(), 3)
                binding.scrollView.setPadding(0,0,0,150)
            }
        }
        binding.tvNewAll.setOnClickListener {
            if (binding.tvNewAll.text=="Ortga"){
                binding.tvNewAll.text="Hammasi"
                binding.layoutRecomendation.visibility=View.VISIBLE
                binding.layoutNewAll.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                binding.rvYangi.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            }
            else{
            binding.tvNewAll.text="Ortga"
                binding.layoutRecomendation.visibility=View.GONE
                binding.scrollView.setPadding(0,0,0,150)
                binding.rvYangi.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
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
                Thread{
                    Thread.sleep(2000)
                    doubleBackPressed = false
                }.start()
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

