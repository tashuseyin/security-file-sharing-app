package com.example.encryptedimagesharingapp.model.firestore

import android.app.Activity
import android.util.Log
import com.example.encryptedimagesharingapp.model.models.User
import com.example.encryptedimagesharingapp.ui.activities.LoginActivity
import com.example.encryptedimagesharingapp.ui.activities.RegisterActivity
import com.example.encryptedimagesharingapp.ui.activities.UserProfileActivity
import com.example.encryptedimagesharingapp.utils.Constants
import com.example.encryptedimagesharingapp.utils.DataPrefer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class FireStore {
    private val db = Firebase.firestore
    private lateinit var dataPrefer: DataPrefer

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        db.collection(Constants.USERS)
            .document(userInfo.uuid)
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
        db.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.i("TAG", document.toString())
                val user = document.toObject<User>()


                when (activity) {
                    is LoginActivity -> {
                        user?.let {
                            activity.userLoggedInSuccess(it)
                        }
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


    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {

        db.collection(Constants.USERS).document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        activity.updateProfileSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while while updating the user details.",
                    e
                )
            }
    }

}