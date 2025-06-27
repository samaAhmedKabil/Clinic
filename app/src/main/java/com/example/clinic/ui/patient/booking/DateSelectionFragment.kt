package com.example.clinic.ui.patient.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.databinding.FragmentDateSelectionBinding
import com.example.clinic.ui.dialogs.TellDisabledDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateSelectionFragment: Fragment() {
    private var _binding: FragmentDateSelectionBinding? = null
    private val binding get() = _binding!!

    private var selectedDate: Calendar = Calendar.getInstance()
    private var lastValidSelectedDate: Calendar = Calendar.getInstance() // To store the last valid date

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backArrowClick()
        setupCalendarView()
        confirmClick()

        if (isDayOfWeekInvalid(selectedDate)) {
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

            if (isDayOfWeekInvalid(newlySelectedCalendar)) {
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

    private fun isDayOfWeekInvalid(calendar: Calendar): Boolean {
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek == Calendar.MONDAY || dayOfWeek == Calendar.TUESDAY
    }

    private fun findNextValidDay(calendar: Calendar) {
        var tempCalendar = calendar.clone() as Calendar
        while (isDayOfWeekInvalid(tempCalendar)) {
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
