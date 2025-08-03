package com.example.clinic.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.clinic.databinding.DialogLogoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ConfirmCancelingDialog: BottomSheetDialogFragment() {
    private var _binding: DialogLogoutBinding? = null
    private val binding get() = _binding!!
    private var onDeleteConfirmed: (() -> Unit)? = null
    private lateinit var database: DatabaseReference
    var bookingId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogLogoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logout.text = "الغاء الحجز"
        binding.logoutDes.text = "هل أنت متأكد من الغاء هذا الحجز؟"
        bookingId?.let { okClick(it) }
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }
    fun okClick(bookingId: String){
        binding.ok.setOnClickListener {
            database = FirebaseDatabase.getInstance().reference.child("bookings")
            database.child(bookingId).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "تم الغاء الحجز", Toast.LENGTH_SHORT).show()
                    onDeleteConfirmed?.invoke()
                    dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "فشل الغاء الحجز", Toast.LENGTH_SHORT).show()
                }
        }
    }
    fun setOnDeleteConfirmedListener(callback: () -> Unit) {
        onDeleteConfirmed = callback
    }
}