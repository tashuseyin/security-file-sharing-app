package com.example.encryptedimagesharingapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import com.example.encryptedimagesharingapp.R
import com.example.encryptedimagesharingapp.databinding.ActivityRegisterBinding
import com.example.encryptedimagesharingapp.model.entities.User
import com.example.encryptedimagesharingapp.model.firestore.FireStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spannableString = SpannableString(binding.textPolicy.text)
        val blue = ForegroundColorSpan(resources.getColor(R.color.login_color))
        spannableString.setSpan(blue, 17, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textPolicy.text = spannableString

        auth = Firebase.auth
        setListeners()
    }

    private fun setListeners() {
        binding.signInText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.buttonRegister.setOnClickListener {
            registerUser()
        }
    }


    private fun registerUser() {
        if (validationRegister()) {
            showDialog()
            val email = binding.email.text.toString().trim { it <= ' ' }
            val password = binding.password.text.toString().trim { it <= ' ' }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideDialog()
                    if (task.isSuccessful) {
                        val currentUser = auth.currentUser
                        currentUser?.let { firebaseCurrentUser ->
                            user = User(
                                firebaseCurrentUser.uid,
                                binding.name.text.toString().trim { it <= ' ' },
                                binding.email.text.toString().trim { it <= ' ' }
                            )
                        }
                        FireStore().registerUser(this@RegisterActivity, user)
                        auth.signOut()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        hideDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun userRegistrationSuccess() {
        hideDialog()
        Toast.makeText(this, getString(R.string.register_successful), Toast.LENGTH_SHORT).show()
    }

    private fun validationRegister(): Boolean {
        return when {
            TextUtils.isEmpty(binding.name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.error_message_name), true)
                false
            }
            TextUtils.isEmpty(binding.email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.error_message_email), true)
                false
            }
            TextUtils.isEmpty(binding.password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.error_message_password), true)
                false
            }
            TextUtils.isEmpty(binding.confirmPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.error_message_confirm_password), true)
                false
            }
            binding.password.text.toString()
                .trim { it <= ' ' } != binding.confirmPassword.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(getString(R.string.error_message_confirm_password_mismatch), true)
                false
            }
            !(binding.checkbox.isChecked) -> {
                showErrorSnackBar(resources.getString(R.string.error_message_agree_policy), true)
                false
            }
            else -> {
                true
            }
        }
    }

}