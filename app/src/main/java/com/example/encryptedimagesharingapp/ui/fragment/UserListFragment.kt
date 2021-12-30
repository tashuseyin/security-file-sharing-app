package com.example.encryptedimagesharingapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.encryptedimagesharingapp.R
import com.example.encryptedimagesharingapp.databinding.FragmentUserListBinding
import com.example.encryptedimagesharingapp.model.entities.User
import com.example.encryptedimagesharingapp.ui.activities.MainActivity
import com.example.encryptedimagesharingapp.ui.adapter.UserAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore.collection("users")
    private lateinit var adapter: UserAdapter
    private val userList = ArrayList<User>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserAdapter { user ->
            findNavController().navigate(UserListFragmentDirections.actionUserListFragmentToSelectFileEncryptFragment(user))
            (activity as MainActivity).hideBottomBar()
        }
        binding.recyclerView.adapter = adapter
        fetchData()
    }

    private fun fetchData() {
        db.addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Error", error.message.toString())
            }
            for (dc: DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    userList.add(dc.document.toObject(User::class.java))
                }
            }
            adapter.submitList(userList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}