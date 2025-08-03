package com.example.clinic.ui.dialogs

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.DialogOperationSelectionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OperationSelectionDialog: BottomSheetDialogFragment() {
    private var _binding: DialogOperationSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogOperationSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.medicalRecord.setOnClickListener {
            binding.medicalRecord.setBackgroundResource(R.drawable.rectangle_selected)
            binding.mdIcon.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.default_pink)
            medicalRecordClick()
        }
        binding.medicalMeasurement.setOnClickListener {
            binding.medicalMeasurement.setBackgroundResource(R.drawable.rectangle_selected)
            binding.mmIcon.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.default_pink)
            medicalMeasurementClick()
        }
    }

    private fun medicalRecordClick() {
        val navController = findNavController()
        try {
            Handler().postDelayed({
                navController.navigate(R.id.manageAvailableDatesFragment)
                dismiss()
            }, 1000)
        } catch (e: IllegalStateException) {
            Log.e("DoctorCaseDetailsDialog", "Navigation failed", e)
        }
    }
    private fun medicalMeasurementClick() {
        val navController = findNavController()
        try {
            Handler().postDelayed({
                navController.navigate(R.id.dateSelectionFragment)
                dismiss()
            }, 1000)
        } catch (e: IllegalStateException) {
            Log.e("DoctorCaseDetailsDialog", "Navigation failed", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}