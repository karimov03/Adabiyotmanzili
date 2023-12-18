package com.example.adabiyotmanzili.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.adabiyotmanzili.Activitys.HomeActivity
import com.example.adabiyotmanzili.Adapters.OnlineAdapter
import com.example.adabiyotmanzili.Adapters.SearchAdapter
import com.example.adabiyotmanzili.ApiServis.ApiClient
import com.example.adabiyotmanzili.Dialogs.ProgressDialogFragment
import com.example.adabiyotmanzili.R
import com.example.adabiyotmanzili.databinding.FragmentSearchBinding
import com.example.adabiyotmanzili.models.BookData
import com.example.adabiyotmanzili.models.Books
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class SearchFragment : Fragment() {
    private lateinit var adapter: SearchAdapter
        private lateinit var list:ArrayList<BookData>
        private lateinit var originalList: List<BookData>
        var doubleBackPressed = false
    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        list = ArrayList()
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(editable: Editable?) {
                list.clear()
                if (editable.toString().isEmpty()) {
                    list.addAll(originalList.flatMap { book -> List(8) { book } })
                } else {
                    val lowerCaseQuery = editable.toString().toLowerCase(Locale.getDefault())
                    list.addAll(originalList.filter { book -> book.name.toLowerCase(Locale.getDefault()).contains(lowerCaseQuery) }
                        .flatMap { book -> List(8) { book } })
                }
                adapter.notifyDataSetChanged()
            }
        })
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
                                originalList = it.results.flatMap { book -> List(8) { book } }
                                list = ArrayList(originalList)
                                try {
                                    adapter=SearchAdapter(requireContext(),list)
                                    binding.rvSearch.adapter=adapter
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

                                binding.edtSearch.isFocusable = false
                                binding.edtSearch.isFocusableInTouchMode = false
                            }
                        } ?: run {
                            Toast.makeText(
                                requireContext(),
                                "Server response is null",
                                Toast.LENGTH_SHORT
                            ).show()
                            progressDialogFragment.dismiss()

                            binding.edtSearch.isFocusable = false
                            binding.edtSearch.isFocusableInTouchMode = false
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Server error: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialogFragment.dismiss()

                        binding.edtSearch.isFocusable = false
                        binding.edtSearch.isFocusableInTouchMode = false
                    }
                }

                override fun onFailure(call: Call<Books>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Internet connection issue: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialogFragment.dismiss()

                    binding.edtSearch.isFocusable = false
                    binding.edtSearch.isFocusableInTouchMode = false
                }
            })
        }
        catch (e:Exception){

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
            homeActivity.updateSearchView()


        }
    }
}