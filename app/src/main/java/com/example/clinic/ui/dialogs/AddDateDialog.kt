package com.example.clinic.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.clinic.databinding.DialogCloseDateBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddDateDialog(
    private val onConfirm: (Int, Int, Int) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: DialogCloseDateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCloseDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.ok.setOnClickListener {
            val year = binding.year.text.toString().toIntOrNull()
            val month = binding.month.text.toString().toIntOrNull()
            val day = binding.day.text.toString().toIntOrNull()

            if (year == null || month == null || day == null ||
                year < 1900 || month !in 1..12 || day !in 1..31) {
                Toast.makeText(requireContext(), "Invalid input, please check year/month/day!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            onConfirm(year, month, day)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}