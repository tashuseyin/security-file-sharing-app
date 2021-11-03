package com.example.encryptedimagesharingapp.ui.adapter

import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.encryptedimagesharingapp.databinding.UserItemBinding
import com.example.encryptedimagesharingapp.model.entities.User

class UserViewHolder(private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(user: User, onItemClickListener: (Int, Boolean) -> Unit) {
        binding.name.text = user.name
        binding.email.text = user.email
//        binding.checkbox.isChecked = user.isChecked

        binding.root.setOnClickListener {
            onItemClickListener(adapterPosition,false)
        }
        binding.checkbox.setOnClickListener {
            onItemClickListener(adapterPosition,true)
        }
    }
}