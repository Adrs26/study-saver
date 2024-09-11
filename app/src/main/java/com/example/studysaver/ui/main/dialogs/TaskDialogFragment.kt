package com.example.studysaver.ui.main.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.studysaver.databinding.DialogTaskBinding
import com.example.studysaver.viewmodels.MainTaskViewModel

class TaskDialogFragment : DialogFragment() {
    private lateinit var binding: DialogTaskBinding
    private lateinit var mainTaskViewModel: MainTaskViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupDialogButton()
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

        mainTaskViewModel.taskTitle.observe(viewLifecycleOwner) {
            binding.taskTitle.setText(it)
        }
        mainTaskViewModel.taskDescription.observe(viewLifecycleOwner) {
            binding.taskDescription.setText(it)
        }
        mainTaskViewModel.taskDeadline.observe(viewLifecycleOwner) {
            binding.taskDeadline.setText(it)
        }
    }

    private fun getTaskTitleAndDescription() {
        val title = binding.taskTitle.text.toString()
        val description = binding.taskDescription.text.toString()
        mainTaskViewModel.setTaskTitleAndDescription(title, description)
    }

    private fun setupDialogButton() {
        binding.cancelButton.setOnClickListener {
            if (binding.taskTitle.text?.isNotEmpty() == true || binding.taskDescription.text?.isNotEmpty() == true || binding.taskDeadline.text?.isNotEmpty() == true) {
                getTaskTitleAndDescription()
                Toast.makeText(requireContext(), "Draft tugas berhasil disimpan", Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }
        binding.saveButton.setOnClickListener {
            mainTaskViewModel.taskTitle.value = ""
            mainTaskViewModel.taskDescription.value = ""
            mainTaskViewModel.taskDeadline.value = ""
            Toast.makeText(requireContext(), "Tugas berhasil disimpan", Toast.LENGTH_SHORT).show()
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
}