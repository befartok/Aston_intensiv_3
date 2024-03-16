package com.example.aston_intensiv_3.recykler

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil

const val CONTENT_PAYLOAD_KEY = "CONTENT_PAYLOAD_KEY"

object ContactDiffUtil : DiffUtil.ItemCallback<ContactModel>() {

    override fun areItemsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: ContactModel, newItem: ContactModel): Any? {
        return if (oldItem.firstName != newItem.firstName) {
            val bundle = Bundle()
            bundle.putString(CONTENT_PAYLOAD_KEY, newItem.firstName)
            bundle
        } else {
            super.getChangePayload(oldItem, newItem)
        }
    }
}