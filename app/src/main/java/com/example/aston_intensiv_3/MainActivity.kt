package com.example.newrecycler

import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_intensiv_3.R
import com.example.aston_intensiv_3.recykler.ContactAdapter
import com.example.aston_intensiv_3.recykler.ContactModel
import com.github.javafaker.Faker
import java.util.Locale

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val recycler by find<RecyclerView>(R.id.recycler)

    private var contacts = mutableListOf<ContactModel>()
    private var tapId: Int = 0
    private var clickedPosition: Int = -1
    private lateinit var adapter: ContactAdapter
    private lateinit var buttonAdd: ImageButton
    private lateinit var buttonDelete: ImageButton
    private lateinit var buttonOkDelete: Button
    private lateinit var buttonCancelDelete: Button
    private var isStartDel = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = ContactAdapter { model ->
            tapId = model.id
            clickedPosition = adapter.clickedPosition
            //Toast.makeText(this, "clickedPosition= $clickedPosition", Toast.LENGTH_SHORT).show()

            clickList()

        }
        recycler.adapter = adapter


        val faker = Faker(Locale("RU"))
        contacts = (1..100).map {
            ContactModel(
                id = it,
                firstName = faker.name().firstName(),
                lastName = faker.name().lastName(),
                phoneNumber = faker.phoneNumber().phoneNumber()
            )
        }.toMutableList()

        adapter.submitList(contacts)


        /*        adapter.submitList(List(100) {
                    ContentModel(
                        it, "Hello", "lastNameTitle",
                        "phoneNumberTitle"
                    )
                })*/


        /*
                recycler.adapter = adapter
                adapter.submitList(List(100) {
                    ContentModel(it, "Hello $it", "lastName",
                         "phoneNumber")
                })
        */

        /*        thread {
                    Thread.sleep(4000)
                    runOnUiThread {
                        adapter.submitList(List(100) {
                            if (it == 4) {
                                ContentModel(
                                    it, "New $it", "New",
                                    "New"
                                )
                            } else {
                                ContentModel(
                                    it, "Hellos $it", "lastNameTitles",
                                    "phoneNumberTitles"
                                )
                            }
                        })
                    }
                }*/


        /*
            thread {
                Thread.sleep(4000)
                runOnUiThread {
                    adapter.submitList(List(100) {
                        if (it == 4) {
                            ContentModel(
                                it, "New $it", "New",
                                "New"
                            )
                        } else {
                            ContentModel(
                                it, "Hellos $it", "lastNameTitles",
                                "phoneNumberTitles"
                            )
                        }
                    })
                }
            }*/

        buttonAdd = findViewById<ImageButton>(R.id.addButton)
        buttonDelete = findViewById<ImageButton>(R.id.deleteButton)
        buttonOkDelete = findViewById<Button>(R.id.okDeleteButton)
        buttonCancelDelete = findViewById<Button>(R.id.cancelDeleteButton)


        click()


    }

    private fun clickList() {
        if (!isStartDel) {
            openDialogEdit(findViewById<View>(R.id.item))
        } //else startDelete()
    }

    private fun startDelete() {
        Toast.makeText(this, "startDelete tapId= $tapId", Toast.LENGTH_SHORT).show()
        // TODO: лишняя ветка . удалить
    }

    private fun click() {


        buttonAdd.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val dialogBinding = inflater.inflate(R.layout.add_dialog, null)

            val myDialog = Dialog(this)
            myDialog.setContentView(dialogBinding)
            myDialog.setCancelable(true)
            myDialog.show()

            val addFirstNameDialET = dialogBinding.findViewById<EditText>(R.id.addFirstNameDialET)
            val addLastNameDialET = dialogBinding.findViewById<EditText>(R.id.addLastNameDialET)
            val addPhoneDialET = dialogBinding.findViewById<EditText>(R.id.addPhoneDialET)
            val okAddDialButton = dialogBinding.findViewById<Button>(R.id.okAddDialButton)
            val cancelAddDialButton = dialogBinding.findViewById<Button>(R.id.cancelAddDialButton)

            okAddDialButton.setOnClickListener {

                contacts = ArrayList(contacts)
                contacts.add(
                    ContactModel(
                        contacts.last().id + 1, "${addFirstNameDialET.text}",
                        "${addLastNameDialET.text}", "${addPhoneDialET.text}"
                    )
                )

                adapter.submitList(contacts)
                myDialog.dismiss()
            }
            cancelAddDialButton.setOnClickListener {
                myDialog.dismiss()
            }

        }
        buttonDelete.setOnClickListener {
            Toast.makeText(this, getString(R.string.setContacts), Toast.LENGTH_SHORT).show()
            isStartDel = true
            buttonAdd.isGone = true
            buttonOkDelete.isGone = false
            buttonCancelDelete.isGone = false

        }
        buttonCancelDelete.setOnClickListener {
            isStartDel = false
            buttonAdd.isGone = false
            buttonOkDelete.isGone = true
            buttonCancelDelete.isGone = true
        }
        buttonOkDelete.setOnClickListener {
            var test = adapter.numPosToDel.toString()

            Toast.makeText(this, "test= $test", Toast.LENGTH_SHORT).show()
            contacts = ArrayList(contacts)
            adapter.listPosToDel.sortDescending()
            adapter.listPosToDel.forEach { contacts.removeAt(it) }

            adapter.submitList(contacts)
            adapter.listPosToDel.clear()

        }
    }


    private fun openDialogEdit(view: View) {

        val inflater = LayoutInflater.from(view.context)

        val dialogBinding = inflater.inflate(R.layout.edit_dialog, null)

        val myDialog = Dialog(view.context)
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.show()

        val idDialTextView = dialogBinding.findViewById<TextView>(R.id.idDialTextView)
        idDialTextView.text = contacts[clickedPosition].id.toString()

        val firstNameDialET = dialogBinding.findViewById<EditText>(R.id.firstNameDialET)
        firstNameDialET.setText(contacts[clickedPosition].firstName)

        val lastNameDialTV = dialogBinding.findViewById<EditText>(R.id.lastNameDialTV)
        lastNameDialTV.setText(contacts[clickedPosition].lastName)

        val phoneDialTV = dialogBinding.findViewById<EditText>(R.id.phoneDialTV)
        phoneDialTV.setText(contacts[clickedPosition].phoneNumber)

        val btnOkDialog = dialogBinding.findViewById<Button>(R.id.okDialButton)
        val btnCancelDialog = dialogBinding.findViewById<Button>(R.id.cancelDialButton)

        btnOkDialog.setOnClickListener {

            contacts = ArrayList(contacts)
            contacts[clickedPosition].firstName = firstNameDialET.text.toString()
            contacts[clickedPosition].lastName = lastNameDialTV.text.toString()
            contacts[clickedPosition].phoneNumber = phoneDialTV.text.toString()

            adapter.notifyItemChanged(clickedPosition)

            adapter.submitList(contacts)
            myDialog.dismiss()
        }
        btnCancelDialog.setOnClickListener {
            myDialog.dismiss()
        }
    }
}

private fun <VIEW : View> AppCompatActivity.find(@IdRes idRes: Int): Lazy<VIEW> {
    return lazy { findViewById(idRes) }
}

