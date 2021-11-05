package com.example.encryptedimagesharingapp.ui.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.encryptedimagesharingapp.databinding.UserItemBinding
import com.example.encryptedimagesharingapp.model.entities.User

class SelectViewHolder(private val binding: UserItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(user: User) {
        binding.name.text = user.name
        binding.email.text = user.email
        binding.checkbox.isVisible = false
    }
}