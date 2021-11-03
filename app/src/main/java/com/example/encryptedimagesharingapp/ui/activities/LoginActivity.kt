package com.example.encryptedimagesharingapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.lifecycleScope
import com.example.encryptedimagesharingapp.R
import com.example.encryptedimagesharingapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    private fun loginRegisteredUser() {
        if (validateLoginDetails()) {
            showDialog()
            val email = binding.email.text.toString().trim { it <= ' ' }
            val password = binding.password.text.toString().trim { it <= ' ' }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser = auth.currentUser
                        lifecycleScope.launch {
                            delay(2000)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }

                    } else {
                        hideDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }


}