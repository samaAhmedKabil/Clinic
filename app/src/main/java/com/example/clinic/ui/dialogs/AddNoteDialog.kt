package com.example.clinic.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clinic.databinding.DialogNoteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNoteDialog(private val onNoteAdded: (String) -> Unit): BottomSheetDialogFragment() {
    private var _binding: DialogNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ok.setOnClickListener {
            val note = binding.editQues.text.toString().trim()
            binding.ok.setOnClickListener {
                if (note.isNotEmpty()) {
                    onNoteAdded(note)
                }
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}