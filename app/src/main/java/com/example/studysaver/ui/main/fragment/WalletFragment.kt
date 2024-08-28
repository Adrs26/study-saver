package com.example.studysaver.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studysaver.databinding.FragmentMainWalletBinding

class WalletFragment : Fragment() {
    private lateinit var binding: FragmentMainWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainWalletBinding.inflate(inflater, container, false)
        return binding.root
    }
}