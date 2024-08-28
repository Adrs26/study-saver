package com.example.studysaver.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studysaver.databinding.FragmentMainHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentMainHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}