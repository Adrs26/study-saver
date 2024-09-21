package com.example.studysaver.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.studysaver.databinding.FragmentMainHomeBinding
import com.example.studysaver.viewmodels.MainTaskViewModel

class MainHomeFragment : Fragment() {
    private lateinit var binding: FragmentMainHomeBinding
    private lateinit var mainTaskViewModel: MainTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupObservers()
    }

    private fun setupViewModel() {
        mainTaskViewModel = ViewModelProvider(requireActivity())[MainTaskViewModel::class.java]
    }

    private fun setupObservers() {
        mainTaskViewModel.undoneTaskCount.observe(viewLifecycleOwner) {
            binding.undoneTaskCount.text = it.toString()
        }
        mainTaskViewModel.lateTaskCount.observe(viewLifecycleOwner) {
            binding.lateTaskCount.text = it.toString()
        }
        mainTaskViewModel.doneTaskCount.observe(viewLifecycleOwner) {
            binding.doneTaskCount.text = it.toString()
        }
    }
}