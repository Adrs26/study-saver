package com.example.studysaver.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studysaver.databinding.FragmentMainTaskBinding

class TaskFragment : Fragment() {
    private lateinit var binding: FragmentMainTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainTaskBinding.inflate(inflater, container, false)
        return binding.root
    }
}