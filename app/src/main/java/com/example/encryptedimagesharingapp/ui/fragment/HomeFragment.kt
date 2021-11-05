package com.example.encryptedimagesharingapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.encryptedimagesharingapp.R
import com.example.encryptedimagesharingapp.databinding.FragmentHomeBinding
import com.example.encryptedimagesharingapp.ui.activities.MainActivity


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            selectFile.setOnClickListener {
                val userListFragment = UserListFragment()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment, userListFragment)?.commit()
                (activity as MainActivity).hideBottomBar()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}