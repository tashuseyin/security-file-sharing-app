package com.example.encryptedimagesharingapp.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    val uuid: String = "",
    val name: String = "",
    val email: String = "",
    val isChecked: Boolean = false
) : Parcelable