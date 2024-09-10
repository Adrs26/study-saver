package com.example.studysaver.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studysaver.databinding.ItemTaskDoneBinding
import com.example.studysaver.databinding.ItemTaskLateBinding
import com.example.studysaver.databinding.ItemTaskUndoneBinding

class TaskAdapter(
    private val context: Context,
    private var taskList: List<Int>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
        const val VIEW_TYPE_THREE = 3
    }

    private var currentViewType: Int = VIEW_TYPE_ONE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ONE -> {
                val binding = ItemTaskUndoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolderUndone(binding)
            }
            VIEW_TYPE_TWO -> {
                val binding = ItemTaskLateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolderLate(binding)
            }
            VIEW_TYPE_THREE -> {
                val binding = ItemTaskDoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolderDone(binding)
            }
            else -> throw IllegalArgumentException("Invalid View Type")
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_ONE -> (holder as ItemViewHolderUndone).bind(taskList[position])
            VIEW_TYPE_TWO -> (holder as ItemViewHolderLate).bind(taskList[position])
            VIEW_TYPE_THREE -> (holder as ItemViewHolderDone).bind(taskList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentViewType
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItemList: List<Int>, newViewType: Int) {
        taskList = newItemList
        currentViewType = newViewType
        notifyDataSetChanged()
    }

    inner class ItemViewHolderUndone(private val itemBinding: ItemTaskUndoneBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Int) {
            itemBinding.checkboxTask.visibility = if (data == 0) View.VISIBLE else View.GONE
        }
    }

    inner class ItemViewHolderLate(private val itemBinding: ItemTaskLateBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Int) {
            itemBinding.checkboxTask.visibility = if (data == 0) View.VISIBLE else View.GONE
        }
    }

    inner class ItemViewHolderDone(private val itemBinding: ItemTaskDoneBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Int) {
            itemBinding.checkboxTask.visibility = if (data == 0) View.VISIBLE else View.GONE
        }
    }
}