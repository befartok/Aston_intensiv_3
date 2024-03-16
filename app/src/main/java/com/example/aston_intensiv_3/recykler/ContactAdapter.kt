package com.example.aston_intensiv_3.recykler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.aston_intensiv_3.databinding.ContactItemBinding

class ContactAdapter(
    private val onClickAction: (ContactModel) -> Unit,
) : ListAdapter<ContactModel, ContactAdapter.ContactViewHolder>(
    AsyncDifferConfig
        .Builder(ContactDiffUtil)
        .build()
) {
    var clickedPosition = -1
    var listPosToDel: MutableList<Int> = mutableListOf<Int>()
    var isStartDel: Boolean = false
    var selects = Array(150) { false }.toBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ContactItemBinding.inflate(inflater, parent, false)
        val holder = ContactViewHolder(binding)

        clickedPosition = holder.adapterPosition

// TODO: убрать тосты
        binding.root.setOnClickListener {

            clickedPosition = holder.adapterPosition
            val model = getItem(holder.adapterPosition)
            onClickAction(model)

            if (isStartDel) {
                if (!model.isSelected) {
                    model.isSelected = true
                    selects[clickedPosition] = true
                    listPosToDel.add(clickedPosition)
                    listPosToDel.sort()
                } else {
                    model.isSelected = false
                    selects[clickedPosition] = false
                    listPosToDel = listPosToDel.filter { it != clickedPosition }.toMutableList()
                    listPosToDel.sort()
                }
                notifyDataSetChanged()
            }
        }
        return holder
    }
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)

        if (selects[position]) {
            holder.itemView.setBackgroundColor(Color.LTGRAY)
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    class ContactViewHolder(private val binding: ContactItemBinding) : ViewHolder(binding.root) {
        fun bind(model: ContactModel) {
            binding.idTextView.text = model.id.toString()
            binding.firstNameTextView.text = model.firstName
            binding.lastNameTextView.text = model.lastName
            binding.phoneNumberTextView.text = model.phoneNumber
        }
    }

}
