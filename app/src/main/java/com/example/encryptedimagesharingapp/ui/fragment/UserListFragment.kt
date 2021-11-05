package com.example.encryptedimagesharingapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.encryptedimagesharingapp.R
import com.example.encryptedimagesharingapp.databinding.FragmentUserListBinding
import com.example.encryptedimagesharingapp.model.entities.User
import com.example.encryptedimagesharingapp.ui.adapter.UserAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore.collection("users")
    private lateinit var adapter: UserAdapter
    private val userList = ArrayList<User>()
    private val isSelectedUserList = ArrayList<User>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserAdapter { position ->
            userList[position] = userList[position].copy(isChecked = !userList[position].isChecked)
        }
        binding.recyclerView.adapter = adapter


        binding.btnOk.setOnClickListener {
            val selectFileEncryptFragment =
                SelectFileEncryptFragment(userList.filter { it.isChecked })
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment, selectFileEncryptFragment)
                ?.commit()
        }

        lifecycleScope.launch {
            fetchData()
        }
    }

    private fun fetchData() {
        db.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        userList.add(dc.document.toObject(User::class.java))
                    }
                }
                adapter.submitList(userList)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}