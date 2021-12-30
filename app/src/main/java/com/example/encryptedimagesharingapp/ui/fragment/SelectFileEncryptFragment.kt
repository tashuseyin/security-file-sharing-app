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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.encryptedimagesharingapp.R
import com.example.encryptedimagesharingapp.databinding.FragmentSelectFileEncryptBinding
import com.example.encryptedimagesharingapp.model.entities.User
import com.example.encryptedimagesharingapp.ui.activities.MainActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class SelectFileEncryptFragment: Fragment() {

    private var _binding: FragmentSelectFileEncryptBinding? = null
    private val binding get() = _binding!!
    private val requestMedia = 100
    private val requestDocument = 101
    private lateinit var fileNames: String
    private var file: Uri? = null
    private var finalNumber: Int = 0
    private val storage = Firebase.storage
    private val storageReference = storage.reference
    private lateinit var user: User

    private val args: SelectFileEncryptFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectFileEncryptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = args.userarg

        binding.name.text = user.name
        binding.email.text = user.email

        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            fileConstraint.setOnClickListener {
                dispatchDocumentIntent("*/*")
            }
            uploadButton.setOnClickListener {
                file?.let { uri ->
                    var os = ByteArrayOutputStream()
                    val inputStream =
                        (activity as MainActivity).contentResolver.openInputStream(uri)
                    val byteArray = inputStream?.readBytes()
                    val newData = ByteArray(byteArray?.size!!)
                    var index = 0
                    user.uuid.forEach {
                        finalNumber = (it.toInt() + finalNumber) % 100
                    }
                    byteArray.forEach { byte ->
                        newData.set(index, (byte - finalNumber).toByte())
                        index++
                    }
                    uploadFile(newData)
                }
            }
        }

    }


    private fun uploadFile(byteArray: ByteArray) {
        val filesRef: StorageReference? = storageReference.child("${user.email}/$fileNames")
        (activity as MainActivity).showDialog()
        filesRef?.putBytes(byteArray)
            ?.addOnSuccessListener { _ ->
                (activity as MainActivity).hideDialog()
                Toast.makeText(
                    context,
                    "Your files was upload successfully.",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(SelectFileEncryptFragmentDirections.actionSelectFileEncryptFragmentToHomeFragment())
                (activity as MainActivity).showBottomBar()
            }
            ?.addOnFailureListener { exception ->
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == requestMedia) {
            data?.let {
                file = it.data
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == requestDocument) {
            data?.let {
                file = it.data
            }
        }
        file?.let {
            fileNames = DocumentFile.fromSingleUri(requireContext(), it)?.name.toString()
            binding.fileName.text = fileNames
            binding.imageFile.setImageResource(R.drawable.folder)
            binding.fileSelectText.isVisible = false
        }
    }


    private fun dispatchDocumentIntent(type: String) {
        val intent = Intent()
            .setType(type)
            .setAction(Intent.ACTION_OPEN_DOCUMENT)
        startActivityForResult(Intent.createChooser(intent, "Dosyayı Seçiniz"), requestDocument)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}