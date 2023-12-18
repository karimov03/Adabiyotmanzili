package com.example.adabiyotmanzili.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adabiyotmanzili.R
import com.example.adabiyotmanzili.databinding.ItemDownloadBookBinding
import com.example.adabiyotmanzili.models.BookData
import com.example.adabiyotmanzili.models.Books

class OnlineAdapter(val context: Context,val list: List<BookData>):RecyclerView.Adapter<OnlineAdapter.vh>() {

    class vh(val itembook:ItemDownloadBookBinding):RecyclerView.ViewHolder(itembook.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vh {
        return vh(ItemDownloadBookBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int =list.size
        override fun onBindViewHolder(holder: vh, position: Int) {
            val book=list[position]
            holder.itembook.bookName.text=book.name
            holder.itembook.bookAfterName.text=book.author
            Glide.with(context)
                .load(book.image)
                .error(R.drawable.image_book)
                .into(holder.itembook.imageBook)



            holder.itembook.bookName.text=book.name
    }

}