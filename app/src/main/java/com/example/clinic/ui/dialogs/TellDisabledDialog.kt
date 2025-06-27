package com.example.clinic.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clinic.databinding.DialogDisabledBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TellDisabledDialog: BottomSheetDialogFragment() {

    private var _binding: DialogDisabledBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogDisabledBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ok.setOnClickListener {
            dismiss()
        }
    }
}