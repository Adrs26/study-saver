package com.example.studysaver.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studysaver.adapters.HistoryAdapter
import com.example.studysaver.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupButton()
        setupRecyclerView()
    }

    private fun setupBinding() {
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupButton() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter(mutableListOf(1, 1, 1, 1))
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter
    }
}