package com.example.clinic.ui.patient.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.clinic.data.DisabledDate
import com.example.clinic.databinding.FragmentPatientDateSelectionBinding
import com.example.clinic.repos.DisabledDatesRepo
import com.example.clinic.ui.dialogs.TellDisabledDialog
import com.example.clinic.ui.doctor.manageBookings.DisabledDatesViewModel
import com.example.clinic.utils.ConstData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateSelectionFragment: Fragment() {
    private var _binding: FragmentPatientDateSelectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var userId: String
    private var selectedDate: Calendar = Calendar.getInstance()
    private var lastValidSelectedDate: Calendar = Calendar.getInstance() // To store the last valid date
    private lateinit var disabledViewModel: DisabledDatesViewModel
    private var disabledDates: List<DisabledDate> = emptyList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientDateSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backArrowClick()
        setupCalendarView()
        confirmClick()

        val factory = DisabledDatesViewModel.Factory(DisabledDatesRepo())
        disabledViewModel = ViewModelProvider(this, factory)[DisabledDatesViewModel::class.java]
        disabledViewModel.loadDisabledDates()

        disabledViewModel.disabledDates.observe(viewLifecycleOwner) { list ->
            disabledDates = list
        }

        if (isDateInvalid(selectedDate)) {
            findNextValidDay(selectedDate)
            binding.calender.date = selectedDate.timeInMillis
        }
        lastValidSelectedDate = selectedDate.clone() as Calendar

        updateSelectedDateText(selectedDate)
    }

    private fun backArrowClick() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupCalendarView() {
        binding.calender.setOnDateChangeListener { view: CalendarView, year: Int, month: Int, dayOfMonth: Int ->
            val newlySelectedCalendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            if (isDateInvalid(newlySelectedCalendar)) {
                showDisabledDayDialog()
                binding.calender.date = lastValidSelectedDate.timeInMillis
                selectedDate = lastValidSelectedDate
            } else {
                selectedDate = newlySelectedCalendar
                lastValidSelectedDate = newlySelectedCalendar.clone() as Calendar
            }
            updateSelectedDateText(selectedDate)
        }
    }

    private fun isDateInvalid(calendar: Calendar): Boolean {
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek == Calendar.MONDAY || dayOfWeek == Calendar.TUESDAY) return true

        return disabledDates.any {
            it.year == calendar.get(Calendar.YEAR) &&
                    it.month == calendar.get(Calendar.MONTH) + 1 && // Firebase month saved 1-based
                    it.day == calendar.get(Calendar.DAY_OF_MONTH)
        }
    }

    private fun findNextValidDay(calendar: Calendar) {
        var tempCalendar = calendar.clone() as Calendar
        while (isDateInvalid(tempCalendar)) {
            tempCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        calendar.timeInMillis = tempCalendar.timeInMillis
    }


    private fun updateSelectedDateText(calendar: Calendar) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        binding.tvSelectedDate.text = "Selected Date: $formattedDate"
    }

    private fun confirmClick(){
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        userRef.get().addOnSuccessListener { snapshot ->
            val role = snapshot.child("role").value?.toString() ?: ""
            if(role == ConstData.DOCTOR_TYPE){
                binding.btnConfirm.setOnClickListener {
                    findNavController().navigate(DateSelectionFragmentDirections.actionDateSelectionFragmentToManageBookingsFragment(date = selectedDate.timeInMillis))
                }
            }
        }
        binding.btnConfirm.setOnClickListener {
            findNavController().navigate(DateSelectionFragmentDirections.actionDateSelectionFragmentToSlotSelectionFragment(date = selectedDate.timeInMillis))
        }
    }

    private fun showDisabledDayDialog() {
        val bottomSheetFragment = TellDisabledDialog()
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
