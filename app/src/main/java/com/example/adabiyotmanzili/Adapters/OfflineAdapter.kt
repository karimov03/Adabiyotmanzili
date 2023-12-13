package com.example.adabiyotmanzili.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adabiyotmanzili.R
import com.example.adabiyotmanzili.databinding.ItemDownloadBookBinding
import com.example.adabiyotmanzili.models.OfflineFile

class OfflineAdapter(val context: Context, var list: List<OfflineFile>) : RecyclerView.Adapter<OfflineAdapter.vh>() {

    class vh(val itemDownloadBookBinding: ItemDownloadBookBinding) : RecyclerView.ViewHolder(itemDownloadBookBinding.root) {

        fun bind(book: OfflineFile) {
            itemDownloadBookBinding.bookName.text = book.file_name
            itemDownloadBookBinding.bookAfterName.text = book.file_uri

                itemDownloadBookBinding.imageBook.setImageResource(R.drawable.image_book)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vh {
        return vh(ItemDownloadBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size



    override fun onBindViewHolder(holder: vh, position: Int) {
        holder.bind(list[position])
    }
}
