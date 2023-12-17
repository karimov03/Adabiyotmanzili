package com.example.adabiyotmanzili.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adabiyotmanzili.R
import com.example.adabiyotmanzili.databinding.ItemDownloadBookBinding
import com.example.adabiyotmanzili.models.OfflineFile

class OfflineAdapter(
    private val context: Context,
    private var fileList: List<OfflineFile>,
    private val rvAction: RvAction
) : RecyclerView.Adapter<OfflineAdapter.ViewHolder>() {

    inner class ViewHolder(private val itemBinding: ItemDownloadBookBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(book: OfflineFile) {
            itemBinding.bookName.text = book.file_name
            itemBinding.bookAfterName.text = "%.2f MB".format(book.file_size.toFloat()!! / (1024 * 1024))
            // Agar thumbnail     mavjud bo'lsa, uningni o'rnatamiz
            if (book.file_thumpnail != null) {
                itemBinding.imageBook.setImageBitmap(book.file_thumpnail)
            } else {
                // Agar thumbnail bo'sh bo'lsa, default rasmni o'rnatamiz
                itemBinding.imageBook.setImageResource(R.drawable.image_book)
            }

            // Itemga click bo'lganda
            itemBinding.root.setOnClickListener {
                rvAction.OnRootClick(fileList, adapterPosition)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDownloadBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = fileList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(fileList[position])
    }

    interface RvAction {
        fun OnRootClick(list: List<OfflineFile>, position: Int)
    }
}
