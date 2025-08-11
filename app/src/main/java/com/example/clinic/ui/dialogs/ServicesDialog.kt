package com.example.clinic.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clinic.data.Service
import com.example.clinic.databinding.DialogServicesBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ServicesDialog(
    private val existingService: Service?,
    private val onSave: (Service) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: DialogServicesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogServicesBinding.inflate(inflater, container, false)

        // If editing an existing service, fill fields
        existingService?.let {
            binding.editService.setText(it.name)
            binding.editPrice.setText(it.price.toString())
        }

        // Save button click
        binding.ok.setOnClickListener {
            val name = binding.editService.text.toString().trim()
            val price = binding.editPrice.text.toString().toDoubleOrNull() ?: 0.0

            if (name.isNotEmpty()) {
                val service = existingService?.copy(
                    name = name,
                    price = price
                ) ?: Service(name = name, price = price)

                onSave(service)
                dismiss()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
