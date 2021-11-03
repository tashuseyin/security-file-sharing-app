package com.example.encryptedimagesharingapp.model.firestore

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.encryptedimagesharingapp.model.entities.User
import com.example.encryptedimagesharingapp.ui.activities.LoginActivity
import com.example.encryptedimagesharingapp.ui.activities.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
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

    private fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserUUID = ""
        if (currentUser != null) {
            currentUserUUID = currentUser.uid
        }
        return currentUserUUID
    }

    fun getUserDetails(activity: Activity) {
        db.document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.i("TAG", document.toString())
                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences("name_prefer", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("logged_in_username", "${user.name}")
                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                }
            }
            .addOnFailureListener {
                when (activity) {
                    is LoginActivity -> {
                        activity.hideDialog()
                    }
                }
            }
    }


}