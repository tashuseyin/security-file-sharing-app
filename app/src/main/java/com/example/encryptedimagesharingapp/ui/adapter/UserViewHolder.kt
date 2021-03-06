package com.example.encryptedimagesharingapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.encryptedimagesharingapp.databinding.UserItemBinding
import com.example.encryptedimagesharingapp.model.entities.User

class UserViewHolder(private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(user: User, onItemClickListener: (user:User) -> Unit) {
        binding.name.text = user.name
        binding.email.text = user.email

        binding.root.setOnClickListener {
            onItemClickListener(user)
        }
    }
}