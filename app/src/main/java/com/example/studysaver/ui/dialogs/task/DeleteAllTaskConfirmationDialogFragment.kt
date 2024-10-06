package com.example.studysaver.ui.dialogs.task

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.studysaver.databinding.DialogDeleteAllTaskConfirmationBinding
import com.example.studysaver.viewmodels.MainTaskViewModel

class DeleteAllTaskConfirmationDialogFragment : DialogFragment() {
    private lateinit var binding: DialogDeleteAllTaskConfirmationBinding
    private lateinit var mainTaskViewModel: MainTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDeleteAllTaskConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.setCancelable(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupButton()
    }

    private fun setupViewModel() {
        mainTaskViewModel = ViewModelProvider(requireActivity())[MainTaskViewModel::class.java]
    }

    private fun setupButton() {
        binding.cancelButton.setOnClickListener {
            mainTaskViewModel.updateCheckedStatus(false)
            dismiss()
        }
        binding.submitButton.setOnClickListener {
            mainTaskViewModel.deleteTaskByStatus()
            mainTaskViewModel.onCloseDeleteTaskClicked()
            dismiss()
            Toast.makeText(requireContext(), "Tugas berhasil dihapus", Toast.LENGTH_SHORT).show()
        }
    }
}