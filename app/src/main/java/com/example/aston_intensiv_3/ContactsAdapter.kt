package com.example.aston_intensiv_3

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_intensiv_3.databinding.ItemContactBinding
import com.example.aston_intensiv_3.recykler.Contact

interface ContactActionListener {

    fun onContactMove(contact: Contact, moveBy: Int)

    fun onContactDelete(contact: Contact)

    fun onContactDetails(contact: Contact)

}


class ContactDiffCallback(
    private val oldList: List<Contact>,
    private val newList: List<Contact>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContact = oldList[oldItemPosition]
        val newContact = newList[newItemPosition]
        return oldContact.id == newContact.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContact = oldList[oldItemPosition]
        val newContact = newList[newItemPosition]
        return oldContact == newContact
    }
}

class ContactsAdapter(
    private val actionListener: ContactActionListener
) : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>(), View.OnClickListener {

    var contacts: List<Contact> = emptyList()
        set(newValue) {
            val diffCallback = ContactDiffCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onClick(v: View) {
        val contact = v.tag as Contact
        when (v.id) {
            R.id.moreImageViewButton -> {
                showPopupMenu(v)
            }

            else -> {
                actionListener.onContactDetails(contact)
            }
        }
    }

    override fun getItemCount(): Int = contacts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.moreImageViewButton.setOnClickListener(this)

        return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = contacts[position]
        with(holder.binding) {
            holder.itemView.tag = contact
            moreImageViewButton.tag = contact

            firstNameTextView.text = contact.firstName
            lastNameTextView.text = contact.lastName
            phoneNumberTextView.text = contact.phoneNumber

        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        val contact = view.tag as Contact
        val position = contacts.indexOfFirst { it.id == contact.id }

        popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, context.getString(R.string.move_up)).apply {
            isEnabled = position > 0
        }
        popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, context.getString(R.string.move_down))
            .apply {
                isEnabled = position < contacts.size - 1
            }
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove))

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_MOVE_UP -> {
                    actionListener.onContactMove(contact, -1)
                }

                ID_MOVE_DOWN -> {
                    actionListener.onContactMove(contact, 1)
                }

                ID_REMOVE -> {
                    actionListener.onContactDelete(contact)
                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    class ContactsViewHolder(
        val binding: ItemContactBinding
    ) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
    }
}