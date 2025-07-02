package com.example.clinic.ui.doctor.manageBookings

import SlotsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic.R
import com.example.clinic.databinding.FragmentDoctorManageBookingsBinding
import com.example.clinic.repos.BookingRepo
import com.example.clinic.ui.patient.booking.BookingViewModel
import com.example.clinic.ui.patient.booking.SlotSelectionFragmentArgs
import com.example.clinic.ui.patient.booking.SlotSelectionFragmentDirections
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.getValue

class ManageBookingsFragment:Fragment() {
    private var _binding: FragmentDoctorManageBookingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ManageBookingsViewModel
    private lateinit var slotAdapter: SlotsAdapter
    private val args: SlotSelectionFragmentArgs by navArgs()

    lateinit var timeSlots: List<String>

    private lateinit var receivedSelectedDate: Calendar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoctorManageBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDoctorManageBookingsBinding.bind(view)

        val repo = BookingRepo()
        val factory = ManageBookingsViewModel.ManageBookingsViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[ManageBookingsViewModel::class.java]

        val receivedDateMillis = args.date
        receivedSelectedDate = Calendar.getInstance().apply { timeInMillis = receivedDateMillis }

        displayReceivedDate(receivedSelectedDate)

        setupRecyclerView()
        setupObservers()
        setupButtonListeners()
        backArrowClick()

        val formattedDateForViewModel = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(receivedSelectedDate.time)
        viewModel.setSelectedDate(formattedDateForViewModel)
        viewModel.loadAvailableSlots(formattedDateForViewModel)
        showRecyclerViewWithAnimation()
    }

    private fun backArrowClick() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun displayReceivedDate(calendar: Calendar) {
        val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault()) // Adjust format as needed
        val formattedDate = dateFormat.format(calendar.time)
        binding.tvSelectedDate.text = "Selected Date: $formattedDate"
    }

    private fun setupRecyclerView() {
        timeSlots = if (isAmDay()) {
            listOf(
                "10:00 AM", "10:10 AM", "10:20 AM", "10:30 AM", "10:40 AM", "10:50 AM",
                "11:00 AM", "11:10 AM", "11:20 AM", "11:30 AM", "11:40 AM", "11:50 AM",
                "12:00 PM", "12:10 PM", "12:20 PM", "12:30 PM", "12:40 PM", "12:50 PM",
                "6:00 PM", "6:10 PM", "6:20 PM", "6:30 PM", "6:40 PM", "6:50 PM",
                "7:00 PM", "7:10 PM", "7:20 PM", "7:30 PM", "7:40 PM", "7:50 PM",
                "8:00 PM", "8:10 PM", "8:20 PM", "8:30 PM", "8:40 PM", "8:50 PM",
                "9:00 PM", "9:10 PM", "9:20 PM", "9:30 PM", "9:40 PM", "9:50 PM"
            )
        } else {
            listOf(
                "6:00 PM", "6:10 PM", "6:20 PM", "6:30 PM", "6:40 PM", "6:50 PM",
                "7:00 PM", "7:10 PM", "7:20 PM", "7:30 PM", "7:40 PM", "7:50 PM",
                "8:00 PM", "8:10 PM", "8:20 PM", "8:30 PM", "8:40 PM", "8:50 PM",
                "9:00 PM", "9:10 PM", "9:20 PM", "9:30 PM", "9:40 PM", "9:50 PM"
            )
        }

        slotAdapter = SlotsAdapter(timeSlots) { slot ->
            viewModel.setSelectedSlot(slot)
        }

        binding.rvSlots.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = slotAdapter
            layoutAnimation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_slide_in)
            setPadding(8, 8, 8, 8)
        }
    }


    private fun isAmDay(): Boolean {
        val dayOfWeek = receivedSelectedDate.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek == Calendar.WEDNESDAY || dayOfWeek == Calendar.SATURDAY
    }

    private fun showRecyclerViewWithAnimation() {
        if (binding.rvSlots.visibility != View.VISIBLE) {
            binding.rvSlots.visibility = View.VISIBLE
            val slideDown = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down)
            slideDown.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    binding.rvSlots.scheduleLayoutAnimation()
                }
                override fun onAnimationRepeat(animation: Animation?) {}
            })
            binding.rvSlots.startAnimation(slideDown)
        } else {
            binding.rvSlots.scheduleLayoutAnimation()
        }
    }

    private fun setupButtonListeners() {
        binding.btnConfirm.setOnClickListener {
            binding.inProgress.visibility = View.VISIBLE
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
                binding.inProgress.visibility = View.GONE
                findNavController().navigate(ManageBookingsFragmentDirections.actionManageBookingsFragmentToDoctorHomeFragment("DR.Ehab Kabil"))
                viewModel.clearStatus()
            }
        }

        viewModel.bookingError.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                binding.inProgress.visibility = View.GONE
                viewModel.clearStatus()
            }
        }

        viewModel.availableSlots.observe(viewLifecycleOwner) { availableSlots ->
            val bookedSlots = viewModel.bookedSlots.value ?: emptyList()
            slotAdapter.updateSlots(timeSlots, bookedSlots)
            binding.rvSlots.scheduleLayoutAnimation()
        }

        viewModel.bookedSlots.observe(viewLifecycleOwner) { bookedSlots ->
            slotAdapter.updateSlots(timeSlots, bookedSlots)
            binding.rvSlots.scheduleLayoutAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}