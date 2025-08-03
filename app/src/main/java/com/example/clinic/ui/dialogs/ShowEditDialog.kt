package com.example.clinic.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clinic.data.CommonQuestions
import com.example.clinic.databinding.DialogEditQuestionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ShowEditDialog(private val question: CommonQuestions, private val onSave: (String, String) -> Unit): BottomSheetDialogFragment() {

    private var _binding: DialogEditQuestionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogEditQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editQues.setText(question.question)
        binding.editAnswer.setText(question.answer)

        binding.ok.setOnClickListener {
            val newQ = binding.editQues.text.toString().trim()
            val newA = binding.editAnswer.text.toString().trim()
            onSave(newQ, newA)
            dismiss()
        }
    }
}