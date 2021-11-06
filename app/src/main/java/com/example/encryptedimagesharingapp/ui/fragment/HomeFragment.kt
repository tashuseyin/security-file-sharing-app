package com.example.encryptedimagesharingapp.ui.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.encryptedimagesharingapp.R
import com.example.encryptedimagesharingapp.databinding.FragmentHomeBinding
import com.example.encryptedimagesharingapp.ui.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var fileList = ArrayList<String>()
    private val storage = Firebase.storage
    private val storageReference = storage.reference

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
        getData()
    }

    private fun setListeners() {
        binding.apply {
            selectFile.setOnClickListener {
                val userListFragment = UserListFragment()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment, userListFragment)?.commit()
                (activity as MainActivity).hideBottomBar()
            }

            downloadSelect.setOnClickListener {
                downloadSelect.isEnabled = false
                val file = fileList[0].split(".")
                val name = file[0]
                val type = file[1]
                downloadFile(name, type)
                downloadSelect.isEnabled = true
            }
        }
    }


    private fun downloadFile(name: String, type: String) {
        val fileRef: StorageReference? = storageReference.child("${auth.uid}")
        val localFile = File.createTempFile(name, ".${type}")
        fileRef?.getFile(localFile)?.addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            Toast.makeText(context, "Download Successfully.", Toast.LENGTH_SHORT).show()
        }?.addOnFailureListener { exception ->
            Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    private fun getData() {
        val storageReference = storage.reference.child("${auth.uid}")
        storageReference.listAll()
            .addOnSuccessListener { listResult ->
                listResult.items.forEach {
                    fileList.add(it.name)
                }
                if (fileList.isEmpty()){
                    Toast.makeText(context, "Size gönderilmiş dosya bulunmamaktadır.", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(context, "Yeni bir dosyanız var.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error occurred!", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}