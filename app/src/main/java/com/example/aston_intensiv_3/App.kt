package com.example.aston_intensiv_3

import android.app.Application
import com.example.aston_intensiv_3.recykler.ContactsService

class App: Application() {
    val contactsService = ContactsService()
}