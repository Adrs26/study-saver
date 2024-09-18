package com.example.studysaver.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studysaver.adapters.TaskAdapter
import com.example.studysaver.databinding.FragmentMainTaskBinding
import com.example.studysaver.ui.dialogs.TaskDialogFragment
import com.example.studysaver.viewmodels.MainTaskViewModel

class MainTaskFragment : Fragment() {
    companion object {
        const val MENU_UNDONE = 1
        const val MENU_LATE = 2
        const val MENU_DONE = 3
    }

    private lateinit var binding: FragmentMainTaskBinding

    private val mainTaskViewModel: MainTaskViewModel by lazy {
        ViewModelProvider(requireActivity())[MainTaskViewModel::class.java]
    }
    private val taskAdapter: TaskAdapter by lazy {
        TaskAdapter(requireContext(), mutableListOf())
    }

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

        setupMenuButton()
        setupToolbarIcon()
        setupRecyclerView()
        setupObservers()
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

        binding.closeIcon.setOnClickListener {
            mainTaskViewModel.onCloseDeleteTaskClicked()
        }
    }

    private fun setupRecyclerView() {
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.taskRecyclerView.adapter = taskAdapter
    }

    private fun showPopupAddTask() {
        val taskDialogFragment = TaskDialogFragment()
        taskDialogFragment.show(parentFragmentManager, "ALERT_DIALOG_FRAGMENT")
    }

    private fun setupObservers() {
        mainTaskViewModel.menuId.observe(viewLifecycleOwner) {
            taskAdapter.updateData(
                mainTaskViewModel.getTaskList(it),
                mainTaskViewModel.getViewType(it)
            )
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

        mainTaskViewModel.showDeleteTask.observe(viewLifecycleOwner) {
            if (it) {
                updateDeleteTaskView(View.VISIBLE, View.GONE, 0)
            }  else {
                updateDeleteTaskView(View.GONE, View.VISIBLE, 1)
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
        listStatus: Int
    ) {
        updateViewVisibilityForDelete(toolbarIconVisibility, taskActionVisibility)
        mainTaskViewModel.changeTaskList(listStatus)
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
}