package com.example.aston_intensiv_3.recykler

import com.github.javafaker.Faker
import java.util.ArrayList
import java.util.Collections
import java.util.Locale

typealias ContactsListener = (contacts: List<Contact>) -> Unit

class ContactsService {

    private var contacts = mutableListOf<Contact>() // Все пользователи

    private val listeners = mutableSetOf<ContactsListener>()

    init {
        //val faker = Faker.instance()

        val faker = Faker(Locale("RU"))
        contacts= (1..100).map {
            Contact(
                id = it.toLong(),
                firstName = faker.name().firstName(),
                lastName = faker.name().lastName(),
                phoneNumber = faker.phoneNumber().phoneNumber()
            )
        }.toMutableList()
    }

    fun getContacts(): List<Contact> {
        return contacts
    }

    fun deleteContact(contact: Contact) {
        val indexToDelete = contacts.indexOfFirst { it.id == contact.id }
        if (indexToDelete != -1) {
            contacts = ArrayList(contacts)
            contacts.removeAt(indexToDelete)
            notifyChanges()
        }
    }

    fun moveContact(contact: Contact, moveBy: Int) {
        val oldIndex = contacts.indexOfFirst { it.id == contact.id }
        if (oldIndex == -1) return
        val newIndex = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= contacts.size) return
        contacts = ArrayList(contacts)
        Collections.swap(contacts, oldIndex, newIndex)
        notifyChanges()
    }

    fun addListener(listener: ContactsListener) {
        listeners.add(listener)
        listener.invoke(contacts)
    }

    fun removeListener(listener: ContactsListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(contacts) }
    }

}

