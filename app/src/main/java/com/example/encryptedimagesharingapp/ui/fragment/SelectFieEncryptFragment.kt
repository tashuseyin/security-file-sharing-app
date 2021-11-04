package com.example.encryptedimagesharingapp.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import com.example.encryptedimagesharingapp.R
import com.example.encryptedimagesharingapp.databinding.FragmentSelectFileEncryptBinding
import com.example.encryptedimagesharingapp.ui.activities.MainActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class SelectFieEncryptFragment : Fragment() {

    private var _binding: FragmentSelectFileEncryptBinding? = null
    private val binding get() = _binding!!
    private val requestMedia = 100
    private val requestDocument = 101
    private lateinit var file: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectFileEncryptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                binding.fileName.text = "document"
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == requestMedia && data != null) {
            file = data.data!!
        }

        if (resultCode == Activity.RESULT_OK && requestCode == requestDocument && data != null) {
            file = data.data!!
        }
        binding.imageFile.setImageURI(file)
        binding.fileName.text = DocumentFile.fromSingleUri(requireContext(),file)?.name
    }

    private fun dispatchTakePictureGalleryIntent(type: String) {
        Dexter.withContext(context)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    openGallery(type)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(
                        context,
                        "You have denied storage permission.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(context, "Error occurred!", Toast.LENGTH_SHORT).show()
            }.onSameThread()
            .check()
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