package com.example.aston_intensiv_3.recykler

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.isGone
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
    var numPosToDel = -1
    var clickedPosition = -1
    var listPosToDel: MutableList<Int> = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContactItemBinding.inflate(inflater, parent, false)
        val holder = ContactViewHolder(binding)
        clickedPosition = holder.adapterPosition

        binding.root.setOnClickListener {

            clickedPosition = holder.adapterPosition
            val model = getItem(holder.adapterPosition)
            onClickAction(model)
        }


        binding.checkBox.setOnClickListener {

            val checkBox = binding.checkBox
            clickedPosition = holder.adapterPosition
            numPosToDel = holder.adapterPosition
            Toast.makeText(parent.context, "numPosToDel = $numPosToDel", Toast.LENGTH_SHORT)
                .show()
            if (checkBox.isChecked) {
                numPosToDel = holder.adapterPosition
                listPosToDel.add(numPosToDel)
                listPosToDel.sort()
                Toast.makeText(parent.context, "listPosToDel = $listPosToDel", Toast.LENGTH_SHORT)
                    .show()
            } else {
                numPosToDel = holder.adapterPosition

                listPosToDel = listPosToDel.filter { it != numPosToDel }.toMutableList()
                listPosToDel.sort()
                Toast.makeText(
                    parent.context,
                    "listPosToDelEmpty = $listPosToDel",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }
        return holder
    }


    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)

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
