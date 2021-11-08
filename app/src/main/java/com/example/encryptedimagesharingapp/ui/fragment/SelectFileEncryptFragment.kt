package com.example.encryptedimagesharingapp.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import com.example.encryptedimagesharingapp.R
import com.example.encryptedimagesharingapp.databinding.FragmentSelectFileEncryptBinding
import com.example.encryptedimagesharingapp.model.entities.User
import com.example.encryptedimagesharingapp.ui.activities.MainActivity
import com.example.encryptedimagesharingapp.util.Util
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class SelectFileEncryptFragment(private val user: User) : Fragment() {

    private var _binding: FragmentSelectFileEncryptBinding? = null
    private val binding get() = _binding!!
    private val requestMedia = 100
    private val requestDocument = 101
    private lateinit var fileNames: String
    private var file: Uri? = null
    private val storage = Firebase.storage
    private val storageReference = storage.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectFileEncryptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.name.text = user.name
        binding.email.text = user.email

        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            back.setOnClickListener {
                (activity as MainActivity).showBottomBar()
                val homeFragment = HomeFragment()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment, homeFragment)?.commit()
            }
            addImage.setOnClickListener {
                dispatchTakePictureGalleryIntent("image/*")
            }

            addVideo.setOnClickListener {
                dispatchTakePictureGalleryIntent("video/*")
            }
            addDocument.setOnClickListener {
                dispatchDocumentIntent("application/*")
            }
            uploadButton.setOnClickListener {
                file?.let {
                    uploadFile(it)
                }
            }
        }

    }


    private fun uploadFile(file: Uri) {
        val filesRef: StorageReference? = storageReference.child("${user.uuid}/$fileNames")
        (activity as MainActivity).showDialog()
        filesRef?.putFile(file)
            ?.addOnSuccessListener { _ ->
                (activity as MainActivity).hideDialog()
                Toast.makeText(
                    context,
                    "Your files was upload successfully.",
                    Toast.LENGTH_SHORT
                ).show()
                val homeFragment = HomeFragment()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment, homeFragment)
                    ?.commit()
                (activity as MainActivity).showBottomBar()
            }
            ?.addOnFailureListener { exception ->
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == requestMedia) {
            file = data?.data!!
        }

        if (resultCode == Activity.RESULT_OK && requestCode == requestDocument) {
            file = data?.data!!
        }
        file?.let {
            fileNames = DocumentFile.fromSingleUri(requireContext(), it)?.name.toString()
            binding.fileName.text = fileNames
            binding.imageFile.setImageResource(R.drawable.folder)
            binding.fileSelectText.isVisible = false
        }
    }


    private fun dispatchTakePictureGalleryIntent(type: String) {
        Util.dispatchTakePictureGalleryIntent(requireContext()) {
            when (it) {
                Util.PermissionType.GRANTED -> {
                    openGallery(type)
                }
            }
        }
    }


    private fun dispatchDocumentIntent(type: String) {
        val intent = Intent()
            .setType(type)
            .setAction(Intent.ACTION_OPEN_DOCUMENT)
        startActivityForResult(Intent.createChooser(intent, "Dosyayı Seçiniz"), requestDocument)
    }


    private fun openGallery(type: String) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = type
        startActivityForResult(intent, requestMedia)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}