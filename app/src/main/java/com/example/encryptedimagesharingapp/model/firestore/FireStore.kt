package com.example.encryptedimagesharingapp.model.firestore

import android.util.Log
import com.example.encryptedimagesharingapp.model.entities.User
import com.example.encryptedimagesharingapp.ui.activities.RegisterActivity
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FireStore {

    private val db = Firebase.firestore.collection("users")

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        db.document(userInfo.uuid)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideDialog()
                Log.e("TAG", "Error while registering the user.", e)
            }
    }
}