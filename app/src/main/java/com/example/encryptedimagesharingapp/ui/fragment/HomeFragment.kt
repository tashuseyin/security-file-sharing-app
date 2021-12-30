package com.example.encryptedimagesharingapp.ui.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.encryptedimagesharingapp.R
import com.example.encryptedimagesharingapp.databinding.FragmentHomeBinding
import com.example.encryptedimagesharingapp.ui.activities.LoginActivity
import com.example.encryptedimagesharingapp.ui.activities.MainActivity
import com.example.encryptedimagesharingapp.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var file: List<String>
    private val db = Firebase.firestore.collection("users")
    private val storage = Firebase.storage
    private val storageReference = storage.reference
    private lateinit var key: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            selectFile.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToUserListFragment())
                (activity as MainActivity).hideBottomBar()
            }
            logout.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            downloadSelect.setOnClickListener {
                downloadSelect.isEnabled = false
                getData()
                downloadSelect.isEnabled = true
            }
        }
    }


    private fun downloadFile(name: String, type: String) {
        val rootPath =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath)
        if (!rootPath.exists()) {
            rootPath.mkdirs()
        }
        val fileRef: StorageReference =
            storageReference.child("${auth.currentUser!!.email}/$name.$type")
        val localFile = File(rootPath, "temp$name.$type")
        fileRef.getFile(localFile)
            .addOnSuccessListener {
                (activity as MainActivity).hideDialog()
                Util.decryptFile(requireContext(), localFile, auth.uid)
                Toast.makeText(context, "Download Successfully.", Toast.LENGTH_SHORT).show()
                removeFile(name, type)
            }
            .addOnFailureListener { exception ->
                (activity as MainActivity).hideDialog()
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            }
    }


    private fun getData() {
        val storageReference = storage.reference.child("${auth.currentUser!!.email}")
        (activity as MainActivity).showDialog()
        storageReference.listAll()
            .addOnSuccessListener { listResult ->
                if (listResult.items.isEmpty()) {
                    (activity as MainActivity).hideDialog()
                    Toast.makeText(
                        context,
                        "Şuan size gönderilmiş bir dosya bulunmamaktadır.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    file = listResult.items[0].name.split(".")
                    val name = file[0]
                    val type = file[1]
                    downloadFile(name, type)
                }

            }
            .addOnFailureListener {
                (activity as MainActivity).hideDialog()
                Toast.makeText(context, "Error occurred!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeFile(name: String, type: String) {
        val storageReference = storage.reference.child("${auth.currentUser!!.email}/$name.$type")
        storageReference.delete()
            .addOnSuccessListener {
                Toast.makeText(context, "File successfully deleted.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}