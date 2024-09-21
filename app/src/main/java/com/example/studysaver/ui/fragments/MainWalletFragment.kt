package com.example.studysaver.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.studysaver.databinding.FragmentMainWalletBinding
import com.example.studysaver.ui.HistoryActivity
import com.example.studysaver.viewmodels.MainWalletViewModel

class MainWalletFragment : Fragment() {
    private lateinit var binding: FragmentMainWalletBinding
    private lateinit var mainWalletViewModel: MainWalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupMenuButton()
        setupToolbarIcon()
        setupObservers()
    }

    private fun setupViewModel() {
        mainWalletViewModel = ViewModelProvider(requireActivity())[MainWalletViewModel::class.java]
    }

    private fun setupMenuButton() {
        binding.allTransactionButton.setOnClickListener {
            mainWalletViewModel.changeMenuButton(MENU_ALL_TRANSACTION)
        }
        binding.incomeButton.setOnClickListener {
            mainWalletViewModel.changeMenuButton(MENU_INCOME)
        }
        binding.expenseButton.setOnClickListener {
            mainWalletViewModel.changeMenuButton(MENU_EXPENSE)
        }
    }

    private fun setupToolbarIcon() {
        binding.historyIcon.setOnClickListener {
            startActivity(Intent(requireContext(), HistoryActivity::class.java))
        }
    }

    private fun setupObservers() {
        mainWalletViewModel.allTransactionButtonStyle.observe(viewLifecycleOwner) { (background, textColor) ->
            setupButtonStyle(binding.allTransactionButton, background, textColor)
        }
        mainWalletViewModel.incomeButtonStyle.observe(viewLifecycleOwner) { (background, textColor) ->
            setupButtonStyle(binding.incomeButton, background, textColor)
        }
        mainWalletViewModel.expenseButtonStyle.observe(viewLifecycleOwner) { (background, textColor) ->
            setupButtonStyle(binding.expenseButton, background, textColor)
        }
    }

    private fun setupButtonStyle(button: TextView, background: Int, textColor: Int) {
        button.background = if (background == 0) null else ContextCompat.getDrawable(
            requireContext(),
            background
        )
        button.setTextColor(ContextCompat.getColor(requireContext(), textColor))
    }

    companion object {
        const val MENU_ALL_TRANSACTION = 1
        const val MENU_INCOME = 2
        const val MENU_EXPENSE = 3
    }
}