package com.example.encryptedimagesharingapp.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.encryptedimagesharingapp.R
import com.example.encryptedimagesharingapp.databinding.ActivityMainBinding
import com.example.encryptedimagesharingapp.ui.fragment.HomeFragment
import com.example.encryptedimagesharingapp.ui.fragment.UserListFragment
import com.google.firebase.auth.FirebaseAuth
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar
import github.com.st235.lib_expandablebottombar.MenuItemDescriptor

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomMenu()
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

