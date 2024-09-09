package com.example.studysaver.ui.main.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studysaver.R
import com.example.studysaver.adapters.TaskAdapter
import com.example.studysaver.databinding.FragmentMainTaskBinding
import com.example.studysaver.databinding.PopupTaskBinding
import com.example.studysaver.listeners.task.OnCloseButtonClickListener
import com.example.studysaver.listeners.task.OnDeleteButtonClickListener
import com.example.studysaver.viewmodels.MainTaskViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskFragment : Fragment() {
    private lateinit var binding: FragmentMainTaskBinding
    private lateinit var mainTaskViewModel: MainTaskViewModel
    private lateinit var taskAdapter: TaskAdapter
    private var list: MutableList<Int> = mutableListOf()
    private var deleteListener: OnDeleteButtonClickListener? = null
    private var closeListener: OnCloseButtonClickListener? = null

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
        setupTaskMenuButton()
        setupRecyclerView()
        setupPopupAddTask()
        setupToolbarIcon()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            deleteListener = context as OnDeleteButtonClickListener
            closeListener = context as OnCloseButtonClickListener
        } catch (e: ClassCastException) {
            Log.e("TaskFragment", "Listener not Implemented")
        }
    }

    override fun onDetach() {
        super.onDetach()
        deleteListener = null
        closeListener = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupViewModel() {
        mainTaskViewModel = ViewModelProvider(requireActivity())[MainTaskViewModel::class.java]

        mainTaskViewModel.menuId.observe(viewLifecycleOwner) {
            when (it) {
                1 -> {
                    list = mutableListOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
                    taskAdapter.updateData(list, TaskAdapter.VIEW_TYPE_ONE)
                }
                2 -> {
                    list = mutableListOf(1, 1)
                    taskAdapter.updateData(list, TaskAdapter.VIEW_TYPE_TWO)
                }
                3 -> {
                    list = mutableListOf(1, 1, 1)
                    taskAdapter.updateData(list, TaskAdapter.VIEW_TYPE_THREE)
                }
            }
        }
        mainTaskViewModel.undoneButtonData.observe(viewLifecycleOwner) { (background, textColor) ->
            setupButtonStyle(binding.undoneButton, background, textColor)
        }
        mainTaskViewModel.lateButtonData.observe(viewLifecycleOwner) { (background, textColor) ->
            setupButtonStyle(binding.lateButton, background, textColor)
        }
        mainTaskViewModel.doneButtonData.observe(viewLifecycleOwner) { (background, textColor) ->
            setupButtonStyle(binding.doneButton, background, textColor)
        }
    }

    private fun setupButtonStyle(button: TextView, background: Int, textColor: Int) {
        when (background) {
            0 -> button.background = null
            else -> button.background = ContextCompat.getDrawable(requireContext(), background)
        }
        button.setTextColor(ContextCompat.getColor(requireContext(), textColor))
    }

    private fun setupTaskMenuButton() {
        val buttonActive = R.drawable.box_hover_blue
        val buttonInactive = 0
        val textActive = R.color.white
        val textInactive = R.color.sky_blue

        changeButton(binding.undoneButton, 1, buttonActive, textActive, buttonInactive, textInactive, buttonInactive, textInactive)
        changeButton(binding.lateButton, 2, buttonInactive, textInactive, buttonActive, textActive, buttonInactive, textInactive)
        changeButton(binding.doneButton, 3, buttonInactive, textInactive, buttonInactive, textInactive, buttonActive, textActive)
    }

    private fun changeButton(button: TextView, menuId: Int, undoneButton: Int, undoneText: Int, lateButton: Int, lateText: Int, doneButton: Int, doneText: Int) {
        button.setOnClickListener {
            mainTaskViewModel.changeMenuButton(menuId, undoneButton, undoneText, lateButton, lateText, doneButton, doneText)
        }
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(requireContext(), list)
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.taskRecyclerView.adapter = taskAdapter
    }

    private fun setupPopupAddTask() {
        binding.addTaskButton.setOnClickListener {
            showPopupAddTask()
        }
    }

    private fun showPopupAddTask() {
        val binding = PopupTaskBinding.inflate(LayoutInflater.from(requireContext()))
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(binding.root)
        val alertDialog = dialogBuilder.create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        binding.saveButton.setOnClickListener {
            alertDialog.dismiss()
        }

        binding.taskDeadline.setOnClickListener {
            showCalendar(binding.taskDeadline)
        }

        alertDialog.show()
    }

    private fun showCalendar(date: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, setYear, setMonth, setDay ->
                val selectedDate = getString(R.string.selected_date, setYear, setMonth + 1, setDay)
                val indonesianDateFormat = getString(R.string.indonesian_date_format, getDayFromDate(selectedDate), convertDateToIndonesianFormat(selectedDate))
                date.setText(indonesianDateFormat)
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun getDayFromDate(dateString: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-[M][MM]-[d][dd]"))
            val dayOfWeek = date.dayOfWeek
            dayOfWeek.getDisplayName(TextStyle.FULL, Locale("id", "ID"))
        } else {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(dateString)
            val calendar = Calendar.getInstance()
            calendar.time = date!!
            calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale("id", "ID"))!!
        }
    }

    private fun convertDateToIndonesianFormat(dateString: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-[M][MM]-[d][dd]"))
            date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID")))
        } else {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            val date: Date? = inputFormat.parse(dateString)
            outputFormat.format(date!!)
        }
    }

    private fun setupToolbarIcon() {
        binding.deleteIcon.setOnClickListener {
            showIconDeleteTask(View.VISIBLE, View.GONE, 0)
            deleteListener?.onDeleteButtonClicked()
        }

        binding.closeIcon.setOnClickListener {
            showIconDeleteTask(View.GONE, View.VISIBLE, 1)
            closeListener?.onCloseButtonClicked()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showIconDeleteTask(viewOneStatus: Int, viewTwoStatus: Int, listStatus: Int) {
        setVisibility(
            binding.deleteForeverIcon to viewOneStatus,
            binding.selectAllIcon to viewOneStatus,
            binding.closeIcon to viewOneStatus,
            binding.deleteIcon to viewTwoStatus,
            binding.addTaskButton to viewTwoStatus,
            binding.undoneButton to viewTwoStatus,
            binding.lateButton to viewTwoStatus,
            binding.doneButton to viewTwoStatus,
            binding.chooseDeleteItemTextView to viewOneStatus
        )

        list.fill(listStatus)
        taskAdapter.notifyDataSetChanged()
    }

    private fun setVisibility(vararg views: Pair<View, Int>) {
        views.forEach { (view, visibility) ->
            view.visibility = visibility
        }
    }
}