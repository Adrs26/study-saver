package com.example.studysaver.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studysaver.databinding.FragmentMainLibraryBinding

class MainLibraryFragment : Fragment() {
    private lateinit var binding: FragmentMainLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }
}