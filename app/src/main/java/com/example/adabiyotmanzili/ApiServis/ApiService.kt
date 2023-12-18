package com.example.adabiyotmanzili.ApiServis

import com.example.adabiyotmanzili.models.BookData
import com.example.adabiyotmanzili.models.Books
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/v1/book/")
    fun getBooks(): Call<Books>
}
