package com.example.encryptedimagesharingapp.model.storage

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class Storage {

    private val storage = Firebase.storage
    private val storageReference = storage.reference

    fun uploadFile(context: Context, directory: String, filename: String, file: Uri) {
        val filesRef: StorageReference? = storageReference.child("$directory/$filename")
        filesRef?.putFile(file)
            ?.addOnSuccessListener { task ->
                task.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Toast.makeText(
                            context,
                            "Your files was upload successfully: $uri",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener { exception ->
                        Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                    }
            }
    }

}