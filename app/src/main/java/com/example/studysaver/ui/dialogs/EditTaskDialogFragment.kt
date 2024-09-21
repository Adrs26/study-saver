package com.example.studysaver.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.studysaver.databinding.DialogEditTaskBinding
import com.example.studysaver.utils.DateTimeUtil
import com.example.studysaver.utils.TaskUtil
import com.example.studysaver.viewmodels.MainTaskViewModel

class EditTaskDialogFragment : DialogFragment() {
    private lateinit var binding: DialogEditTaskBinding
    private lateinit var mainTaskViewModel: MainTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupDialogButton()
        setupObservers()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.setCancelable(false)
    }

    private fun setupViewModel() {
        mainTaskViewModel = ViewModelProvider(requireActivity())[MainTaskViewModel::class.java]
    }

    private fun setupDialogButton() {
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        binding.saveButton.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Tugas berhasil diupdate",
                Toast.LENGTH_SHORT).show()
            mainTaskViewModel.updateTask(
                TaskUtil.taskId.toLong(),
                binding.taskTitle.text.toString(),
                binding.taskDescription.text.toString(),
                binding.taskDeadline.text.toString(),
                mainTaskViewModel.taskDeadline.value ?: 0L)
            dismiss()
        }
        binding.finishButton.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Tugas berhasil diselesaikan",
                Toast.LENGTH_SHORT).show()
            mainTaskViewModel.updateTaskStatus(
                TaskUtil.taskId.toLong(),
                DateTimeUtil.getIndonesianDateFormat())
            dismiss()
        }
        binding.taskDeadline.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialogFragment = DatePickerDialogFragment().apply {
            onDateSetListener = { year, month, day ->
                mainTaskViewModel.setTaskDeadline(year, month, day)
            }
        }
        datePickerDialogFragment.show(parentFragmentManager, "DATE_PICKER_DIALOG")
    }

    private fun setupObservers() {
        mainTaskViewModel.getTaskById(TaskUtil.taskId.toLong()).observe(viewLifecycleOwner) {
            if (it != null) {
                binding.taskTitle.setText(it.title)
                binding.taskDescription.setText(it.description)
                binding.taskDeadline.setText(it.deadline)
            }
        }

        mainTaskViewModel.taskTitleDialog.observe(viewLifecycleOwner) {
            binding.taskTitle.setText(it)
        }
        mainTaskViewModel.taskDescriptionDialog.observe(viewLifecycleOwner) {
            binding.taskDescription.setText(it)
        }
        mainTaskViewModel.taskDeadlineDialog.observe(viewLifecycleOwner) {
            binding.taskDeadline.setText(it)
        }
    }
}