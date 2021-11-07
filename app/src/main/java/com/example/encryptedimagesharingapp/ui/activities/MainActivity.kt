package com.example.encryptedimagesharingapp.ui.activities

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.encryptedimagesharingapp.R
import com.example.encryptedimagesharingapp.databinding.ActivityMainBinding
import com.example.encryptedimagesharingapp.ui.fragment.HomeFragment
import com.example.encryptedimagesharingapp.ui.fragment.UserListFragment
import com.google.firebase.auth.FirebaseAuth
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar
import github.com.st235.lib_expandablebottombar.MenuItemDescriptor

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createFilePermission()
        bottomMenu()
    }

    private fun createFilePermission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(
                        this@MainActivity,
                        "You have denied storage permission.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show()
            }.onSameThread()
            .check()
    }

    private var dialog: Dialog? = null

    fun showDialog() {
        dialog = Dialog(this)
        dialog?.let {
            it.apply {
                setContentView(R.layout.dialog)
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                show()
            }
        }
    }

    fun hideDialog() {
        dialog?.dismiss()
    }

    private fun bottomMenu() {
        val bottomBar: ExpandableBottomBar = binding.expandableBottomBar
        val menu = bottomBar.menu
        menu.add(
            MenuItemDescriptor.Builder(
                this,
                R.id.home,
                R.drawable.ic_baseline_home_24,
                R.string.home,
                Color.BLUE
            )
                .build()
        )
        menu.add(
            MenuItemDescriptor.Builder(
                this,
                R.id.users,
                R.drawable.ic_baseline_person_24,
                R.string.users,
                Color.BLUE
            )
                .build()
        )
        menu.add(
            MenuItemDescriptor.Builder(
                this,
                R.id.logout,
                R.drawable.ic_baseline_login_24,
                R.string.logout,
                Color.BLUE
            )
                .build()
        )

        bottomBar.onItemSelectedListener = { _, menuItem, _ ->
            when (menuItem.id) {
                R.id.home -> {
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment, homeFragment)
                        .commit()
                }
                R.id.users -> {
                    val userListFragment = UserListFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment, userListFragment)
                        .commit()
                }
                R.id.logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    fun hideBottomBar() {
        binding.expandableBottomBar.isVisible = false
    }

    fun showBottomBar() {
        binding.expandableBottomBar.isVisible = true
    }
}

