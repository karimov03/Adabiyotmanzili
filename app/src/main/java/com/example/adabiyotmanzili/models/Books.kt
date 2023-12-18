package com.example.adabiyotmanzili.models

data class Books(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<BookData>
)