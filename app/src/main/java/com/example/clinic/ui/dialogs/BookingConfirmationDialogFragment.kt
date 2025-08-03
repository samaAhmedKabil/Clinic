package com.example.clinic.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clinic.R
import com.example.clinic.databinding.DialogBookingConfirmationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BookingConfirmationDialogFragment : BottomSheetDialogFragment() {
    private var _binding: DialogBookingConfirmationBinding? = null
    private val binding get() = _binding!!

    // Keys for arguments
    companion object {
        const val ARG_SLOT_TIME = "slot_time"

        fun newInstance(slotTime: String): BookingConfirmationDialogFragment {
            val fragment = BookingConfirmationDialogFragment()
            val args = Bundle().apply {
                putString(ARG_SLOT_TIME, slotTime)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogBookingConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val slotTime = arguments?.getString(ARG_SLOT_TIME) ?: "your chosen slot"

        binding.tvConfirmationMessage.text = getString(R.string.booking_confirmed_dialog_message, slotTime)
        binding.tvAdditionalInfo.text = getString(R.string.booking_dialog_additional_info)

        binding.ok.setOnClickListener {
            dismiss()
        }
    }
}