package com.example.studysaver.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studysaver.R
import com.example.studysaver.adapters.TaskAdapter
import com.example.studysaver.databinding.FragmentMainTaskBinding
import com.example.studysaver.ui.dialogs.task.DeleteAllTaskConfirmationDialogFragment
import com.example.studysaver.ui.dialogs.task.DeleteTaskConfirmationDialogFragment
import com.example.studysaver.ui.dialogs.task.AddTaskDialogFragment
import com.example.studysaver.utils.TaskUtil
import com.example.studysaver.viewmodels.MainTaskViewModel

class MainTaskFragment : Fragment() {
    private lateinit var binding: FragmentMainTaskBinding
    private lateinit var mainTaskViewModel: MainTaskViewModel
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupMenuButton()
        setupToolbarIcon()
        setupRecyclerView()
        checkAndUpdateTaskStatus()
        setupObservers()
    }

    private fun setupViewModel() {
        mainTaskViewModel = ViewModelProvider(requireActivity())[MainTaskViewModel::class.java]
    }

    private fun setupMenuButton() {
        binding.undoneButton.setOnClickListener {
            mainTaskViewModel.changeMenuButton(MENU_UNDONE)
        }
        binding.lateButton.setOnClickListener {
            mainTaskViewModel.changeMenuButton(MENU_LATE)
        }
        binding.doneButton.setOnClickListener {
            mainTaskViewModel.changeMenuButton(MENU_DONE)
        }
        binding.addTaskButton.setOnClickListener {
            showPopupAddTask()
        }
    }

    private fun setupToolbarIcon() {
        binding.deleteIcon.setOnClickListener {
            mainTaskViewModel.onDeleteTaskClicked()
        }
        binding.deleteForeverIcon.setOnClickListener {
            if (TaskUtil.taskDeleteList.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Harap pilih tugas yang ingin dihapus",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                showPopupDeleteTaskConfirmation()
            }
        }
        binding.selectAllIcon.setOnClickListener {
            mainTaskViewModel.updateCheckedStatus(true)
            showPopupDeleteAllTaskConfirmation()
        }
        binding.closeIcon.setOnClickListener {
            mainTaskViewModel.onCloseDeleteTaskClicked()
            mainTaskViewModel.updateCheckedStatus(false)
            TaskUtil.taskDeleteList.clear()
        }
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(requireContext(), mutableListOf())
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.taskRecyclerView.adapter = taskAdapter
    }

    private fun checkAndUpdateTaskStatus() {
        mainTaskViewModel.checkAndUpdateTaskStatus()
    }

    private fun showPopupAddTask() {
        val addTaskDialogFragment = AddTaskDialogFragment()
        addTaskDialogFragment.show(parentFragmentManager, "ADD_TASK_DIALOG")
    }

    private fun showPopupDeleteTaskConfirmation() {
        val deleteTaskConfirmationDialogFragment = DeleteTaskConfirmationDialogFragment()
        deleteTaskConfirmationDialogFragment.show(parentFragmentManager, "DELETE_TASK_DIALOG")
    }

    private fun showPopupDeleteAllTaskConfirmation() {
        val deleteAllTaskConfirmationDialogFragment = DeleteAllTaskConfirmationDialogFragment()
        deleteAllTaskConfirmationDialogFragment.show(parentFragmentManager, "DELETE_ALL_TASK_DIALOG")
    }

    private fun setupObservers() {
        mainTaskViewModel.menuId.observe(viewLifecycleOwner) {
            mainTaskViewModel.switchTaskSource(it)
            taskAdapter.updateDataView(mainTaskViewModel.getViewType(it))

            when (it) {
                1 -> binding.emptyTaskTextView.text = resources.getString(R.string.empty_undone_task)
                2 -> binding.emptyTaskTextView.text = resources.getString(R.string.empty_late_task)
                3 -> binding.emptyTaskTextView.text = resources.getString(R.string.empty_done_task)
            }
        }

        mainTaskViewModel.undoneButtonStyle.observe(viewLifecycleOwner) { (background, textColor) ->
            setupButtonStyle(binding.undoneButton, background, textColor)
        }
        mainTaskViewModel.lateButtonStyle.observe(viewLifecycleOwner) { (background, textColor) ->
            setupButtonStyle(binding.lateButton, background, textColor)
        }
        mainTaskViewModel.doneButtonStyle.observe(viewLifecycleOwner) { (background, textColor) ->
            setupButtonStyle(binding.doneButton, background, textColor)
        }

        mainTaskViewModel.undoneTaskCount.observe(viewLifecycleOwner) {
            binding.undoneTaskCount.text = it.toString()
        }
        mainTaskViewModel.lateTaskCount.observe(viewLifecycleOwner) {
            binding.lateTaskCount.text = it.toString()
        }
        mainTaskViewModel.doneTaskCount.observe(viewLifecycleOwner) {
            binding.doneTaskCount.text = it.toString()
        }

        mainTaskViewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.updateDataItem(it)

            if (it.isEmpty()) {
                binding.taskRecyclerView.visibility = View.GONE
                binding.emptyTaskTextView.visibility = View.VISIBLE
            } else {
                binding.taskRecyclerView.visibility = View.VISIBLE
                binding.emptyTaskTextView.visibility = View.GONE
            }
        }

        mainTaskViewModel.showDeleteTask.observe(viewLifecycleOwner) {
            if (it) {
                updateDeleteTaskView(View.VISIBLE, View.GONE, true)
            }  else {
                updateDeleteTaskView(View.GONE, View.VISIBLE, false)
            }
        }
    }

    private fun setupButtonStyle(button: TextView, background: Int, textColor: Int) {
        button.background = if (background == 0) null else ContextCompat.getDrawable(
            requireContext(),
            background
        )
        button.setTextColor(ContextCompat.getColor(requireContext(), textColor))
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateDeleteTaskView(
        toolbarIconVisibility: Int,
        taskActionVisibility: Int,
        readyToDeleteStatus: Boolean
    ) {
        updateViewVisibilityForDelete(toolbarIconVisibility, taskActionVisibility)
        mainTaskViewModel.updateReadyToDeleteStatus(readyToDeleteStatus)
        taskAdapter.notifyDataSetChanged()
    }

    private fun updateViewVisibilityForDelete(
        toolbarIconVisibility: Int,
        taskActionVisibility: Int
    ) {
        setVisibility(
            binding.deleteForeverIcon to toolbarIconVisibility,
            binding.selectAllIcon to toolbarIconVisibility,
            binding.closeIcon to toolbarIconVisibility,
            binding.deleteIcon to taskActionVisibility,
            binding.addTaskButton to taskActionVisibility,
            binding.undoneButton to taskActionVisibility,
            binding.lateButton to taskActionVisibility,
            binding.doneButton to taskActionVisibility,
            binding.chooseDeleteItemTextView to toolbarIconVisibility
        )
    }

    private fun setVisibility(vararg views: Pair<View, Int>) {
        views.forEach { (view, visibility) ->
            view.visibility = visibility
        }
    }

    companion object {
        const val MENU_UNDONE = 1
        const val MENU_LATE = 2
        const val MENU_DONE = 3
    }
}