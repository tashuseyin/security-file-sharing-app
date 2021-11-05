package com.example.encryptedimagesharingapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.encryptedimagesharingapp.databinding.UserItemBinding
import com.example.encryptedimagesharingapp.model.entities.User

class SelectFileAdapter(private val userList: List<User>) :
    RecyclerView.Adapter<SelectViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount() = userList.size

}