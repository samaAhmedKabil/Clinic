package com.example.clinic.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.clinic.databinding.DialogBabyNameBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddBabyNameDialog(
    private val onConfirm: (String, String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: DialogBabyNameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogBabyNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnConfirm.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val gender = when {
                binding.radioMale.isChecked -> "boy"
                binding.radioFemale.isChecked -> "girl"
                else -> ""
            }
            if (name.isEmpty() || gender.isEmpty()) {
                Toast.makeText(requireContext(), "من فضلك ادخل كل البيانات المطلوبة", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            onConfirm(name, gender)
            dismiss()
        }
    }
}