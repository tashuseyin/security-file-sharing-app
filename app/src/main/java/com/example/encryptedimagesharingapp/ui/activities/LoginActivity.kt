package com.example.encryptedimagesharingapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.example.encryptedimagesharingapp.R
import com.example.encryptedimagesharingapp.databinding.ActivityLoginBinding
import com.example.encryptedimagesharingapp.model.firestore.FireStore
import com.example.encryptedimagesharingapp.model.models.User
import com.example.encryptedimagesharingapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_EncryptedImageSharingApp)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        setListeners()
    }

    private fun setListeners() {
        binding.signUpText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        binding.buttonLogin.setOnClickListener {
            loginRegisteredUser()
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.error_message_email), true)
                false
            }
            TextUtils.isEmpty(binding.password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.error_message_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun userLoggedInSuccess(user: User) {
        hideDialog()

        Log.i("Name", user.name)
        Log.i("Mail", user.email)


        if (user.profileCompleted == 0) {
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)

        } else {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        finish()
    }


    private fun loginRegisteredUser() {
        if (validateLoginDetails()) {
            showDialog()
            val email = binding.email.text.toString().trim { it <= ' ' }
            val password = binding.password.text.toString().trim { it <= ' ' }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FireStore().getUserDetails(this@LoginActivity)
                } else {
                hideDialog()
                showErrorSnackBar(task.exception!!.message.toString(), true)
            }
        }
    }
}


}