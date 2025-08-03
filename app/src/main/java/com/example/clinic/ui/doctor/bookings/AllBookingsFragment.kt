package com.example.clinic.ui.doctor.bookings

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic.R
import com.example.clinic.adapters.DoctorBookingAdapter
import com.example.clinic.databinding.FragmentDoctorBookingsBinding
import com.example.clinic.repos.PatientRepo
import com.example.clinic.ui.doctor.viewModel.DoctorViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.getValue

class AllBookingsFragment : Fragment() {
    private var _binding: FragmentDoctorBookingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DoctorViewModel by viewModels { DoctorViewModel.Factory(PatientRepo()) }
    private lateinit var bookingAdapter: DoctorBookingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctor_bookings , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDoctorBookingsBinding.bind(view)
        //viewModel.fetchAllBookings()
        backArrowClick()
        init()
        fetchBookingsByToday()
        calendarClick()
        setupObservers()
        //fetchAllBookings()
    }

    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun init() {
        bookingAdapter = DoctorBookingAdapter(mutableListOf()) { booking, newState ->
            viewModel.updateBookingFinalState(booking.bookingId, newState) { success ->
                if (success) {
                    android.widget.Toast.makeText(requireContext(), "Updated successfully", android.widget.Toast.LENGTH_SHORT).show()
                    // refresh list (better to update the specific item in the list instead of reload)
                    viewModel.fetchBookingsByDate(booking.date)
                } else {
                    android.widget.Toast.makeText(requireContext(), "Failed to update", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.bookedSlots.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bookingAdapter
        }
    }

    private fun fetchBookingsByToday() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModel.fetchBookingsByDate(today)
    }

    private fun calendarClick() {
        binding.calender.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                    binding.inProgress.visibility = View.VISIBLE
                    viewModel.fetchBookingsByDate(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
    }

    private fun setupObservers() {
       binding.inProgress.visibility = View.VISIBLE
        viewModel.bookingsList.observe(viewLifecycleOwner) { bookings ->
            bookingAdapter.updateBookings(bookings)
            binding.inProgress.visibility = View.GONE
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                android.widget.Toast.makeText(context, error, android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}