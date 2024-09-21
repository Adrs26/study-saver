package com.example.studysaver.ui.dialogs

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.studysaver.databases.entities.Task
import com.example.studysaver.databinding.DialogTaskBinding
import com.example.studysaver.viewmodels.MainTaskViewModel

class TaskDialogFragment : DialogFragment() {
    private lateinit var binding: DialogTaskBinding
    private lateinit var mainTaskViewModel: MainTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogTaskBinding.inflate(inflater, container, false)
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mainTaskViewModel.checkAndUpdateTaskStatus()
    }

    private fun setupViewModel() {
        mainTaskViewModel = ViewModelProvider(requireActivity())[MainTaskViewModel::class.java]
    }

    private fun setupDialogButton() {
        binding.cancelButton.setOnClickListener {
            if (binding.taskTitle.text?.isNotEmpty() == true ||
                binding.taskDescription.text?.isNotEmpty() == true ||
                binding.taskDeadline.text?.isNotEmpty() == true
                ) {
                getTaskTitleAndDescription()
                Toast.makeText(
                    requireContext(),
                    "Draft tugas berhasil disimpan",
                    Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }
        binding.saveButton.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Tugas berhasil disimpan",
                Toast.LENGTH_SHORT).show()
            mainTaskViewModel.insert(
                Task(
                    title = binding.taskTitle.text.toString(),
                    description = binding.taskDescription.text.toString(),
                    deadline = binding.taskDeadline.text.toString(),
                    deadlineTimestamp = mainTaskViewModel.taskDeadline.value ?: 0L,
                    status = "Undone",
                    isChecked = false,
                    isReadyToDelete = false,
                    dateFinished = "",
                    dateFinishedTimestamp = 0
                )
            )
            clearDataDialog()
            dismiss()
        }
        binding.taskDeadline.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun getTaskTitleAndDescription() {
        val title = binding.taskTitle.text.toString()
        val description = binding.taskDescription.text.toString()
        mainTaskViewModel.setTaskTitleAndDescription(title, description)
    }

    private fun clearDataDialog() {
        mainTaskViewModel.taskTitleDialog.value = ""
        mainTaskViewModel.taskDescriptionDialog.value = ""
        mainTaskViewModel.taskDeadlineDialog.value = ""
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