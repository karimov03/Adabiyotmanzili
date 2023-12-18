package com.example.adabiyotmanzili.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adabiyotmanzili.R
import com.example.adabiyotmanzili.databinding.ItemDownloadBookBinding
import com.example.adabiyotmanzili.databinding.ItemRvSearchBinding
import com.example.adabiyotmanzili.models.BookData

class SearchAdapter(val context: Context, val list: List<BookData>):RecyclerView.Adapter<SearchAdapter.vh>() {

    class vh(val itembook: ItemRvSearchBinding): RecyclerView.ViewHolder(itembook.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vh {
        return vh(ItemRvSearchBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SearchAdapter.vh, position: Int) {
        val book=list[position]
        holder.itembook.bookName.text=book.name
        holder.itembook.bookAfterName.text=book.author
        Glide.with(context)
            .load(book.image)
            .error(R.drawable.image_book)
            .into(holder.itembook.imageBook)



        holder.itembook.bookName.text=book.name
    }

    override fun getItemCount(): Int =list.size


}