package com.example.studysaver.ui.main

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.studysaver.ui.main.fragment.HomeFragment
import com.example.studysaver.R
import com.example.studysaver.ui.main.fragment.TaskFragment
import com.example.studysaver.ui.main.fragment.WalletFragment
import com.example.studysaver.databinding.ActivityMainBinding
import com.example.studysaver.viewmodels.MainViewModel
import com.example.studysaver.viewmodels.MainViewModelFactory

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val taskFragment = TaskFragment()
        val walletFragment = WalletFragment()

        val factory = MainViewModelFactory(homeFragment)
        val mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        mainViewModel.fragment.observe(this) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, it)
                commit()
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeMenu -> {
                    mainViewModel.changeFragment(homeFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.taskMenu -> {
                    mainViewModel.changeFragment(taskFragment)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    mainViewModel.changeFragment(walletFragment)
                    return@setOnItemSelectedListener true
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }
}