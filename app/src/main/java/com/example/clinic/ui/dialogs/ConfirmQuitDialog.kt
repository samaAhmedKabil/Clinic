package com.example.clinic.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clinic.databinding.DialogLogoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ConfirmQuitDialog: BottomSheetDialogFragment() {
    private var _binding: DialogLogoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogLogoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logout.text = "Quit"
        binding.logoutDes.text = "Are you sure you want to Quit ?"
        binding.cancel.setOnClickListener {
            dismiss()
        }
        binding.ok.setOnClickListener {
            requireActivity().finish()
        }
    }
}