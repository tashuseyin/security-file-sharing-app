package com.example.encryptedimagesharingapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.encryptedimagesharingapp.databinding.FragmentUserListBinding
import com.example.encryptedimagesharingapp.model.entities.User
import com.example.encryptedimagesharingapp.ui.adapter.UserAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore.collection("users")
    private val userList = ArrayList<User>()
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserAdapter { position, isChecked ->
            val currentUser = adapter.currentList[position]
            if (isChecked) {
                currentUser.copy(isChecked = !currentUser.isChecked)
                binding.ok.isVisible = !currentUser.isChecked
            } else {
                binding.ok.isVisible = currentUser.isChecked
            }
        }
        binding.recyclerView.adapter = adapter
        fetchData()

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