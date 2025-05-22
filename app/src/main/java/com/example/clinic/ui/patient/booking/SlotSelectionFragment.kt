package com.example.clinic.ui.patient.booking

import android.os.Bundle
import android.os.Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic.R
import com.example.clinic.repos.BookingRepo
import com.example.clinic.adapters.SlotsAdapter
import com.example.clinic.databinding.FragmentCalenderBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SlotSelectionFragment : Fragment() {
    private var _binding: FragmentCalenderBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BookingViewModel
    private lateinit var slotAdapter: SlotsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = BookingRepo()
        val factory = BookingViewModel.BookingViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[BookingViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        setupDatePicker()
        setupButtonListeners()
        backArrowClick()

        binding.rvSlots.visibility = View.GONE
    }

    private fun backArrowClick() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        val timeSlots = listOf(
            "6:00 PM", "6:10 PM", "6:20 PM", "6:30 PM", "6:40 PM", "6:50 PM",
            "7:00 PM", "7:10 PM", "7:20 PM", "7:30 PM", "7:40 PM", "7:50 PM",
            "8:00 PM", "8:10 PM", "8:20 PM", "8:30 PM", "8:40 PM", "8:50 PM",
            "9:00 PM", "9:10 PM", "9:20 PM", "9:30 PM", "9:40 PM", "9:50 PM", "10:00 PM"
        )

        slotAdapter = SlotsAdapter(timeSlots) { slot ->
            viewModel.setSelectedSlot(slot)
        }

        binding.rvSlots.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = slotAdapter
            // Apply layout animation
            layoutAnimation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_slide_in)
            setPadding(8, 8, 8, 8)
        }
    }

    private fun setupDatePicker() {
        binding.btnOpenDatePicker.setOnClickListener {
            val constraintsBuilder = CalendarConstraints.Builder()
                .setValidator(object : CalendarConstraints.DateValidator {
                    override fun isValid(date: Long): Boolean {
                        val calendar = Calendar.getInstance().apply { timeInMillis = date }
                        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                        return dayOfWeek != Calendar.MONDAY && dayOfWeek != Calendar.TUESDAY
                    }

                    override fun describeContents(): Int = 0
                    override fun writeToParcel(dest: Parcel, flags: Int) {}
                })

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a date")
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

            datePicker.show(parentFragmentManager, "DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener { millis ->
                val calendar = Calendar.getInstance().apply { timeInMillis = millis }
                val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

                if (dayOfWeek == Calendar.MONDAY || dayOfWeek == Calendar.TUESDAY) {
                    Toast.makeText(requireContext(), "Monday and Tuesday are not selectable!", Toast.LENGTH_SHORT).show()
                } else {
                    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(millis))
                    viewModel.setSelectedDate(formattedDate)
                    binding.tvSelectedDate.text = "Selected Date: $formattedDate"
                    // Fetch available slots after setting the date
                    viewModel.loadAvailableSlots(formattedDate)
                    // Show RecyclerView with slide-down animation
                    showRecyclerViewWithAnimation()
                }
            }
        }
    }

    private fun showRecyclerViewWithAnimation() {
        if (binding.rvSlots.visibility != View.VISIBLE) {
            binding.selectFirst.visibility = View.GONE
            binding.rvSlots.visibility = View.VISIBLE
            val slideDown = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down)
            slideDown.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    binding.rvSlots.visibility = View.VISIBLE
                    // Schedule layout animation after RecyclerView slide-down
                    binding.rvSlots.scheduleLayoutAnimation()
                }
                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
    }

    private fun setupButtonListeners() {
        binding.btnConfirm.setOnClickListener {
            viewModel.confirmBooking()
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupObservers() {
        viewModel.bookingStatus.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                Toast.makeText(requireContext(), "Booking Confirmed!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                viewModel.clearStatus()
            }
        }

        viewModel.bookingError.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearStatus()
            }
        }

        viewModel.availableSlots.observe(viewLifecycleOwner) { availableSlots ->
            slotAdapter.updateSlots(availableSlots)
            // Re-trigger layout animation when slots are updated
            binding.rvSlots.scheduleLayoutAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}