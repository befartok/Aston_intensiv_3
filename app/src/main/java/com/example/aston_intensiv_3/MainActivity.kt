package com.example.aston_intensiv_3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aston_intensiv_3.databinding.ActivityMainBinding
import com.example.aston_intensiv_3.recykler.Contact
import com.example.aston_intensiv_3.recykler.ContactsService
import com.example.aston_intensiv_3.recykler.ContactsListener

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ContactsAdapter

    private val contactsService: ContactsService
        get() = (applicationContext as App).contactsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter =  ContactsAdapter(object : ContactActionListener
            {
            override fun onContactMove(contact: Contact, moveBy: Int) {
               contactsService.moveContact(contact, moveBy)
            }

            override fun onContactDelete(contact: Contact) {
               contactsService.deleteContact(contact)
            }

            override fun onContactDetails(contact: Contact) {
                Toast.makeText(this@MainActivity, "Contact: ${contact.firstName}", Toast.LENGTH_SHORT).show()
            }
        }
        )

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        contactsService.addListener(contactsListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        contactsService.removeListener(contactsListener)
    }

    private val contactsListener: ContactsListener = {
        adapter.contacts = it
    }
}