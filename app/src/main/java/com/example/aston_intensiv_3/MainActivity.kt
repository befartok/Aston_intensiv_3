package com.example.aston_intensiv_3

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_intensiv_3.databinding.ActivityMainBinding
import com.example.aston_intensiv_3.databinding.AddDialogBinding
import com.example.aston_intensiv_3.databinding.EditDialogBinding
import com.example.aston_intensiv_3.recykler.ContactAdapter
import com.example.aston_intensiv_3.recykler.ContactModel
import com.github.javafaker.Faker
import java.util.Collections
import java.util.Locale

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val recycler by find<RecyclerView>(R.id.recycler)
    private var contacts = mutableListOf<ContactModel>()
    private var clickedPosition: Int = -1
    private lateinit var adapter: ContactAdapter
    private lateinit var buttonAdd: ImageButton
    private lateinit var buttonDelete: ImageButton
    private lateinit var buttonOkDelete: Button
    private lateinit var buttonCancelDelete: Button
    private var isStartDel = false
    private var isStartAdd = false
    private var isStartEdit = false

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ContactAdapter { model ->
            clickedPosition = adapter.clickedPosition

            clickList()
        }
        recycler.adapter = adapter

        val faker = Faker(Locale("RU"))
        contacts = (1..100).map {
            ContactModel(
                id = it,
                firstName = faker.name().firstName(),
                lastName = faker.name().lastName(),
                phoneNumber = faker.phoneNumber().phoneNumber(),
                isSelected = false
            )
        }.toMutableList()

        adapter.submitList(contacts)

        buttonAdd = binding.addButton
        buttonDelete = binding.deleteButton
        buttonOkDelete = binding.okDeleteButton
        buttonCancelDelete = binding.cancelDeleteButton

        initDragAndDrop()
        click()
    }

    private fun initDragAndDrop() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback
            (ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(
                recycler: RecyclerView,
                source: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePosition = source.adapterPosition
                val targetPosition = target.adapterPosition

                Collections.swap(contacts, sourcePosition, targetPosition)
                adapter.notifyItemMoved(sourcePosition, targetPosition)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        })
        itemTouchHelper.attachToRecyclerView(recycler)
    }


    override fun onSaveInstanceState(outState: Bundle) {

        outState.putParcelableArrayList("contactsKey", contacts as ArrayList<ContactModel>)
        outState.putInt("clickedPositionKey", clickedPosition)
        outState.putBoolean("isStartAddKey", isStartAdd)
        outState.putBoolean("isStartEditKey", isStartEdit)
        outState.putBoolean("isStartDelKey", isStartDel)
        outState.putBoolean("adapterIsStartDelKey", adapter.isStartDel)
        outState.putBooleanArray("adapterSelectsKey", adapter.selects)
        outState.putIntArray("adapterListPosToDelKey", adapter.listPosToDel.toIntArray())

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        contacts = savedInstanceState.getParcelableArrayList("contactsKey")!!
        clickedPosition = savedInstanceState.getInt("clickedPositionKey")
        adapter.submitList(contacts)
        isStartAdd = savedInstanceState.getBoolean("isStartAddKey")
        isStartEdit = savedInstanceState.getBoolean("isStartEditKey")
        isStartDel = savedInstanceState.getBoolean("isStartDelKey")
        adapter.isStartDel = savedInstanceState.getBoolean("adapterIsStartDelKey")
        adapter.selects = savedInstanceState.getBooleanArray("adapterSelectsKey")!!
        val listPosToDel = savedInstanceState.getIntArray("adapterListPosToDelKey")
        if (listPosToDel != null) {
            adapter.listPosToDel = listPosToDel.toMutableList()
        }
        if (isStartDel) {
            startDel()
        }
        if (isStartAdd) {
            openDialogAdd()
        }
        if (isStartEdit) {
            openDialogEdit()
        }
    }

    private fun clickList() {
        if (!isStartDel) {
            openDialogEdit()
        }
    }

    private fun click() {

        buttonAdd.setOnClickListener {
            openDialogAdd()
        }
        buttonDelete.setOnClickListener {
            startDel()
        }
        buttonCancelDelete.setOnClickListener {
            isStartDel = false
            buttonAdd.isGone = false
            buttonOkDelete.isGone = true
            buttonCancelDelete.isGone = true
            adapter.isStartDel = false
        }
        buttonOkDelete.setOnClickListener {
            contacts = ArrayList(contacts)
            adapter.listPosToDel.sortDescending()
            adapter.listPosToDel.forEach { contacts.removeAt(it) }
            adapter.submitList(contacts)
            adapter.listPosToDel.clear()
            adapter.selects = Array(150) { false }.toBooleanArray()
            isStartDel = false
            buttonAdd.isGone = false
            buttonOkDelete.isGone = true
            buttonCancelDelete.isGone = true
            adapter.isStartDel = false
        }
    }
    private fun startDel() {
        Toast.makeText(this, getString(R.string.setContacts), Toast.LENGTH_SHORT).show()
        isStartDel = true
        buttonAdd.isGone = true
        buttonOkDelete.isGone = false
        buttonCancelDelete.isGone = false
        adapter.isStartDel = true
    }

    private fun openDialogAdd() {
        isStartAdd = true
        val dialogBinding = AddDialogBinding.inflate(layoutInflater)

        val myDialog = Dialog(this)
        myDialog.setContentView(dialogBinding.root)
        myDialog.setCancelable(true)
        myDialog.show()

        val addFirstNameDialET = dialogBinding.addFirstNameDialET
        val addLastNameDialET = dialogBinding.addLastNameDialET
        val addPhoneDialET = dialogBinding.addPhoneDialET
        val okAddDialButton = dialogBinding.okAddDialButton
        val cancelAddDialButton = dialogBinding.cancelAddDialButton

        okAddDialButton.setOnClickListener {
            contacts = ArrayList(contacts)
            contacts.add(
                ContactModel(
                    contacts.last().id + 1, "${addFirstNameDialET.text}",
                    "${addLastNameDialET.text}", "${addPhoneDialET.text}", false
                )
            )
            adapter.submitList(contacts)
            isStartAdd = false
            myDialog.dismiss()
        }
        cancelAddDialButton.setOnClickListener {
            isStartAdd = false
            myDialog.dismiss()
        }
    }

    private fun openDialogEdit() {
        isStartEdit = true
        val dialogBinding = EditDialogBinding.inflate(layoutInflater)
        val myDialog = Dialog(this)
        myDialog.setContentView(dialogBinding.root)
        myDialog.setCancelable(true)
        myDialog.show()

        val idDialTextView = dialogBinding.idDialTextView
        idDialTextView.text = contacts[clickedPosition].id.toString()
        val firstNameDialET = dialogBinding.firstNameDialET
        firstNameDialET.setText(contacts[clickedPosition].firstName)
        val lastNameDialTV = dialogBinding.lastNameDialTV
        lastNameDialTV.setText(contacts[clickedPosition].lastName)
        val phoneDialTV = dialogBinding.phoneDialTV
        phoneDialTV.setText(contacts[clickedPosition].phoneNumber)
        val btnOkDialog = dialogBinding.okDialButton
        val btnCancelDialog = dialogBinding.cancelDialButton

        btnOkDialog.setOnClickListener {
            contacts = ArrayList(contacts)
            contacts[clickedPosition].firstName = firstNameDialET.text.toString()
            contacts[clickedPosition].lastName = lastNameDialTV.text.toString()
            contacts[clickedPosition].phoneNumber = phoneDialTV.text.toString()

            adapter.notifyItemChanged(clickedPosition)
            adapter.submitList(contacts)

            isStartEdit = false
            myDialog.dismiss()
        }
        btnCancelDialog.setOnClickListener {
            isStartEdit = false
            myDialog.dismiss()
        }
    }
}

private fun <VIEW : View> AppCompatActivity.find(@IdRes idRes: Int): Lazy<VIEW> {
    return lazy { findViewById(idRes) }
}

