package com.example.encryptedimagesharingapp.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.encryptedimagesharingapp.R
import com.example.encryptedimagesharingapp.databinding.ActivityUserProfileBinding
import com.example.encryptedimagesharingapp.model.firestore.FireStore
import com.example.encryptedimagesharingapp.model.models.User
import com.example.encryptedimagesharingapp.utils.Constants
import java.io.IOException

class UserProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var userDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
        getUserDetails()
    }

    private fun getUserDetails() {
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        binding.apply {
            userProfileName.setText(userDetails.name)
            userProfileEmail.isEnabled = false
            userProfileEmail.setText(userDetails.email)
            userProfileName.isEnabled = false
        }
    }

    private fun setListeners() {
        binding.userProfileImage.setOnClickListener {
            getProfilePhoto()
        }
        binding.buttonProfileSave.setOnClickListener {
            if (validateUserProfileDetails()) {

                val userHashMap = HashMap<String, Any>()
                val phoneNumber = binding.userProfilePhone.text.toString().trim { it <= ' ' }
                val gender = if (binding.genderMale.isChecked) Constants.MALE else Constants.FEMALE

                if (phoneNumber.isNotEmpty()) {
                    userHashMap[Constants.MOBILE] = phoneNumber.toLong()
                }
                userHashMap[Constants.Gender] = gender
                showDialog()

                FireStore().updateUserProfileData(this, userHashMap)

                showErrorSnackBar("Your details are valid. You can update them.", false)
            }
        }
    }

    fun updateProfileSuccess() {
        hideDialog()
        Toast.makeText(
            this,
            getString(R.string.message_profile_update_successs),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.userProfilePhone.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.error_message_phone_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun getProfilePhoto() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showImageChooser()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.READ_STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChooser()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showImageChooser() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, Constants.PICK_IMAGE_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            try {
                val selectedImageFileUri = data.data
                Glide.with(this).load(selectedImageFileUri).centerCrop()
                    .placeholder(R.drawable.person).into(binding.userProfileImage)
                //binding.userProfileImage.setImageURI(selectedImageFileUri)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, getString(R.string.image_selection_failed), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


}

