package com.example.studysaver.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.studysaver.databases.entities.Task
import com.example.studysaver.databinding.ItemTaskDoneBinding
import com.example.studysaver.databinding.ItemTaskLateBinding
import com.example.studysaver.databinding.ItemTaskUndoneBinding
import com.example.studysaver.ui.dialogs.task.EditTaskDialogFragment
import com.example.studysaver.utils.TaskUtil

class TaskAdapter(
    private val context: Context,
    private var taskItems: List<Task>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var currentViewType: Int = VIEW_TYPE_ONE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_ONE -> {
                val binding = ItemTaskUndoneBinding.inflate(inflater, parent, false)
                ItemViewHolderUndone(binding)
            }
            VIEW_TYPE_TWO -> {
                val binding = ItemTaskLateBinding.inflate(inflater, parent, false)
                ItemViewHolderLate(binding)
            }
            VIEW_TYPE_THREE -> {
                val binding = ItemTaskDoneBinding.inflate(inflater, parent, false)
                ItemViewHolderDone(binding)
            }
            else -> throw IllegalArgumentException("Invalid View Type")
        }
    }

    override fun getItemCount(): Int {
        return taskItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_ONE -> (holder as ItemViewHolderUndone).bind(taskItems[position])
            VIEW_TYPE_TWO -> (holder as ItemViewHolderLate).bind(taskItems[position])
            VIEW_TYPE_THREE -> (holder as ItemViewHolderDone).bind(taskItems[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentViewType
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataItem(newItemList: List<Task>) {
        taskItems = newItemList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataView(newViewType: Int) {
        currentViewType = newViewType
        notifyDataSetChanged()
    }

    inner class ItemViewHolderUndone(
        private val itemBinding: ItemTaskUndoneBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Task) {
            itemBinding.checkboxTask.visibility = if (data.isReadyToDelete) View.VISIBLE else View.GONE
            itemBinding.taskTitle.text = data.title
            itemBinding.taskDescription.text = data.description
            itemBinding.taskDeadline.text = data.deadline
            itemBinding.checkboxTask.isChecked = data.isChecked

            itemBinding.checkboxTask.setOnCheckedChangeListener {_, isChecked ->
                if (isChecked) {
                    TaskUtil.taskDeleteList.add(data.id.toLong())
                } else {
                    TaskUtil.taskDeleteList.remove(data.id.toLong())
                }
            }

            itemBinding.rootContainer.setOnClickListener {
                TaskUtil.taskId = data.id
                TaskUtil.taskDeadline = data.deadlineTimestamp
                val dialog = EditTaskDialogFragment()
                dialog.show((context as FragmentActivity).supportFragmentManager, "EDIT_TASK_DIALOG")
            }
        }
    }

    inner class ItemViewHolderLate(
        private val itemBinding: ItemTaskLateBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Task) {
            itemBinding.checkboxTask.visibility = if (data.isReadyToDelete) View.VISIBLE else View.GONE
            itemBinding.taskTitle.text = data.title
            itemBinding.taskDescription.text = data.description
            itemBinding.taskDeadline.text = data.deadline
            itemBinding.checkboxTask.isChecked = data.isChecked

            itemBinding.checkboxTask.setOnCheckedChangeListener {_, isChecked ->
                if (isChecked) {
                    TaskUtil.taskDeleteList.add(data.id.toLong())
                } else {
                    TaskUtil.taskDeleteList.remove(data.id.toLong())
                }
            }

            itemBinding.rootContainer.setOnClickListener {
                TaskUtil.taskId = data.id
                TaskUtil.taskDeadline = data.deadlineTimestamp
                val dialog = EditTaskDialogFragment()
                dialog.show((context as FragmentActivity).supportFragmentManager, "EDIT_TASK_DIALOG")
            }
        }
    }

    inner class ItemViewHolderDone(
        private val itemBinding: ItemTaskDoneBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Task) {
            itemBinding.checkboxTask.visibility = if (data.isReadyToDelete) View.VISIBLE else View.GONE
            itemBinding.taskTitle.text = data.title
            itemBinding.taskDescription.text = data.description
            itemBinding.taskDateCompleted.text = data.dateFinished
            itemBinding.checkboxTask.isChecked = data.isChecked

            itemBinding.checkboxTask.setOnCheckedChangeListener {_, isChecked ->
                if (isChecked) {
                    TaskUtil.taskDeleteList.add(data.id.toLong())
                } else {
                    TaskUtil.taskDeleteList.remove(data.id.toLong())
                }
            }

        }
    }

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
        const val VIEW_TYPE_THREE = 3
    }
}