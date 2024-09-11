package com.example.studysaver.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.studysaver.R
import com.example.studysaver.databinding.ActivityMainBinding
import com.example.studysaver.ui.main.fragments.HomeFragment
import com.example.studysaver.ui.main.fragments.LibraryFragment
import com.example.studysaver.ui.main.fragments.TaskFragment
import com.example.studysaver.ui.main.fragments.WalletFragment
import com.example.studysaver.viewmodels.MainTaskViewModel
import com.example.studysaver.viewmodels.MainViewModel
import com.example.studysaver.viewmodels.MainViewModelFactory

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainTaskViewModel: MainTaskViewModel
    private val homeFragment by lazy { HomeFragment() }
    private val taskFragment by lazy { TaskFragment() }
    private val walletFragment by lazy { WalletFragment() }
    private val libraryFragment by lazy { LibraryFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViewBinding()
        setupViewModel()
        setupBottomNavigation()
        setupOnBackPressed()
    }

    private fun setupViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupViewModel() {
        val factory = MainViewModelFactory(homeFragment)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        mainTaskViewModel = ViewModelProvider(this)[MainTaskViewModel::class.java]

        mainViewModel.fragment.observe(this) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, it)
                commit()
            }
        }
        mainTaskViewModel.showDeleteTask.observe(this) {
            if (it) {
                binding.bottomNavigationView.visibility = View.GONE
            } else {
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.homeMenu -> homeFragment
                R.id.taskMenu -> taskFragment
                R.id.eLibraryMenu -> libraryFragment
                R.id.walletMenu -> walletFragment
                else -> throw IllegalArgumentException("Invalid Item Id")
            }
            mainViewModel.changeFragment(fragment)
            true
        }
    }

    private fun setupOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }
}