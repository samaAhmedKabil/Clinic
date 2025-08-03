package com.example.clinic.ui.patient.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic.R
import com.example.clinic.adapters.BookingAdapter
import com.example.clinic.data.Booking
import com.example.clinic.databinding.FragmentPatientBookedAppointmentsBinding
import com.example.clinic.ui.dialogs.ConfirmCancelingDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookedAppointmentsFragment: Fragment() {
    private var _binding: FragmentPatientBookedAppointmentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var bookingAdapter: BookingAdapter
    private val bookingsList = mutableListOf<Booking>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_patient_booked_appointments , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPatientBookedAppointmentsBinding.bind(view)
        binding.inProgress.visibility = View.VISIBLE
        database = FirebaseDatabase.getInstance().reference.child("bookings")
        bookingAdapter = BookingAdapter(bookingsList) { bookingId ->
            deleteBooking(bookingId)
        }
        binding.bookedSlots.layoutManager = LinearLayoutManager(requireContext())
        binding.bookedSlots.adapter = bookingAdapter
        fetchBookings()
        backArrowClick()
    }

    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun fetchBookings() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookingsList.clear()
                val today = System.currentTimeMillis()
                for (bookingSnapshot in snapshot.children) {
                    val id = bookingSnapshot.key ?: ""
                    val booking = bookingSnapshot.getValue(Booking::class.java)?.copy(id = id)

                    if (booking != null) {
                        // Compare booking.date (as "yyyy-MM-dd") with today
                        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                        val bookingTime = try {
                            sdf.parse(booking.date)?.time ?: 0L
                        } catch (e: Exception) {
                            0L
                        }

                        booking.isDeletable = bookingTime >= today
                        bookingsList.add(booking)
                    }
                }
                bookingAdapter.notifyDataSetChanged()
                binding.inProgress.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                binding.inProgress.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun deleteBooking(bookingId: String) {
        val dialog = ConfirmCancelingDialog()
        dialog.bookingId = bookingId
        dialog.setOnDeleteConfirmedListener {
            bookingAdapter.removeItem(bookingId)
        }
        dialog.show(parentFragmentManager, "ConfirmDialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}