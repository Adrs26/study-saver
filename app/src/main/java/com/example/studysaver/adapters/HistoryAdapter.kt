package com.example.studysaver.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studysaver.databinding.ItemHistoryBinding

class HistoryAdapter(
    private val historyItems: List<Int>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return historyItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(historyItems[position])
    }

    inner class ItemViewHolder(
        private val itemBinding: ItemHistoryBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Int) {

        }
    }
}