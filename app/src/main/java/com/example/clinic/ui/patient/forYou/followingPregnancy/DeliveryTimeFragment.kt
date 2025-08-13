package com.example.clinic.ui.patient.forYou.followingPregnancy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.databinding.FragmentPatientFollowPregnancyBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DeliveryTimeFragment : Fragment() {

    private var _binding: FragmentPatientFollowPregnancyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientFollowPregnancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrowBackClick()
        setupCalendarClick()
    }

    private fun arrowBackClick() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupCalendarClick() {
        binding.calender.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // CalendarView gives zero-based month → adjust
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }

            // Calculate delivery date using Naegele’s Rule
            val deliveryDate = calculateDeliveryDate(selectedDate)

            // Format date
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            // Update the TextView with ID `date`
            binding.date.text = sdf.format(deliveryDate.time)
        }
    }

    private fun calculateDeliveryDate(startDate: Calendar): Calendar {
        return (startDate.clone() as Calendar).apply {
            add(Calendar.YEAR, 1)
            add(Calendar.MONTH, -3)
            add(Calendar.DAY_OF_MONTH, 7)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}