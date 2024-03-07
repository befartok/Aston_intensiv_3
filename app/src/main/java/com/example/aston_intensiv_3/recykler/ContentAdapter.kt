package com.example.aston_intensiv_3.recykler


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
//import ru.aston.lecture3280324.databinding.ContentItemBinding
import com.example.aston_intensiv_3.databinding.ContentItemBinding

/*
class ContentAdapter(
    private val onClickAction: (ContentModel) -> Unit,
) : ListAdapter<ContentModel, ContentAdapter.ContentViewHolder>(
    AsyncDifferConfig
        .Builder(ContentDiffUtil)
        .build()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContentItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener {
            val model = getItem(viewType)
            onClickAction(model)
        }
        return ContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
    }

    override fun onBindViewHolder(
        holder: ContentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads.first() is Bundle) {
            val newContent = payloads.first() as Bundle
            holder.binding.contentTitle.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    holder.binding.contentTitle.text = newContent.getString(CONTENT_PAYLOAD_KEY)
                    holder.binding.contentTitle.animate()
                        .alpha(1f)
                        .setDuration(300)
                }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    class ContentViewHolder(val binding: ContentItemBinding) : ViewHolder(binding.root) {

        fun bind(model: ContentModel) {
            binding.contentTitle.text = model.title
        }
    }
}*/
