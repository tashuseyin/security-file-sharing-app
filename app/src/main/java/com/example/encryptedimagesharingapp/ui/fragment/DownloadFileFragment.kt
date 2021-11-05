package com.example.encryptedimagesharingapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.encryptedimagesharingapp.databinding.FragmentDownloadFileBinding


class DownloadFileFragment : Fragment() {
    private var _binding: FragmentDownloadFileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadFileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun getData() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}