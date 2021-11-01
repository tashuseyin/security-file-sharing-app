package com.example.encryptedimagesharingapp.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.encryptedimagesharingapp.R
import com.example.encryptedimagesharingapp.databinding.ActivityMainBinding
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_EncryptedImageSharingApp)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}