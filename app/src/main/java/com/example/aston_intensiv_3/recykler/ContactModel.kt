package com.example.aston_intensiv_3.recykler

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactModel(
    val id: Int,
    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    var isSelected:Boolean
) : Parcelable{

}