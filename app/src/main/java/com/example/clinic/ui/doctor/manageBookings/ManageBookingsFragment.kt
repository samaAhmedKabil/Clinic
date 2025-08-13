package com.example.clinic.ui.doctor.manageBookings

import SlotsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.clinic.ui.dialogs.AddNoteDialog
import com.example.clinic.ui.dialogs.BookingConfirmationDialogFragment
import com.example.clinic.ui.patient.booking.SlotSelectionFragmentArgs
import com.google.android.material.button.MaterialButton
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

    private val morningSlotsData = listOf(
        "11:00 AM", "11:06 AM", "11:12 AM", "11:18 AM", "11:24 AM", "11:30 AM",
        "11:36 AM", "11:42 AM", "11:48 AM", "11:54 AM", "12:00 PM", "12:06 PM"
    )

    private val eveningSlotsData = listOf(
        "6:00 PM", "6:06 PM", "6:12 PM", "6:18 PM", "6:24 PM", "6:30 PM",
        "6:36 PM", "6:42 PM", "6:48 PM", "6:54 PM",
        "7:00 PM", "7:06 PM", "7:12 PM", "7:18 PM", "7:24 PM", "7:30 PM",
        "7:36 PM", "7:42 PM", "7:48 PM", "7:54 PM",
        "8:00 PM", "8:06 PM", "8:12 PM", "8:18 PM", "8:24 PM", "8:30 PM",
        "8:36 PM", "8:42 PM", "8:48 PM", "8:54 PM",
        "9:00 PM", "9:06 PM", "9:12 PM", "9:18 PM", "9:24 PM", "9:30 PM",
        "9:36 PM", "9:42 PM", "9:48 PM", "9:54 PM"
    )

    private lateinit var receivedSelectedDate: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorManageBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = BookingRepo()
        val factory = ManageBookingsViewModel.ManageBookingsViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[ManageBookingsViewModel::class.java]

        val receivedDateMillis = args.date
        receivedSelectedDate = Calendar.getInstance().apply { timeInMillis = receivedDateMillis }

        displayReceivedDate(receivedSelectedDate)

        setupRecyclerView()
        setupButtonListeners()
        setupObservers()
        backArrowClick()

        val formattedDateForViewModel = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(receivedSelectedDate.time)
        viewModel.setSelectedDate(formattedDateForViewModel)
        viewModel.loadAvailableSlots(formattedDateForViewModel)

        if (isAmDay()) {
            binding.morningSlots.performClick()
        } else {
            binding.eveningSlots.performClick()
        }
    }

    private fun backArrowClick() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun displayReceivedDate(calendar: Calendar) {
        val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        binding.tvSelectedDate.text = "Selected Date: $formattedDate"
    }

    private fun setupRecyclerView() {
        slotAdapter = SlotsAdapter(emptyList()) { slot ->
            viewModel.setSelectedSlot(slot) // This call is correct
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

    private fun setupButtonListeners() {
        binding.morningSlots.setOnClickListener {
            if (isAmDay()) {
                updateButtonBackground(binding.morningSlots)
                updateSlotList(morningSlotsData)
            } else {
                Toast.makeText(requireContext(), "Morning slots are only available on Wednesday and Saturday.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.eveningSlots.setOnClickListener {
            updateButtonBackground(binding.eveningSlots)
            updateSlotList(eveningSlotsData)
        }

        binding.btnConfirm.setOnClickListener {
            binding.inProgress.visibility = View.VISIBLE
            viewModel.confirmBooking()
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.note.setOnClickListener{
            AddNoteDialog { note ->
                viewModel.setBookingNote(note)
                Toast.makeText(requireContext(), "تم اضافة ملحوظات علي الحجز", Toast.LENGTH_SHORT).show()
            }.show(parentFragmentManager, "AddNoteDialog")
        }
    }

    private fun updateButtonBackground(clickedButton: MaterialButton) {
        binding.morningSlots.setBackgroundTintList(resources.getColorStateList(R.color.light_grey, null))
        binding.eveningSlots.setBackgroundTintList(resources.getColorStateList(R.color.light_grey, null))

        clickedButton.setBackgroundTintList(resources.getColorStateList(R.color.default_pink_transparent, null))
    }

    private fun updateSlotList(newSlots: List<String>) {
        val bookedSlots = viewModel.bookedSlots.value ?: emptyList()
        slotAdapter.updateSlots(newSlots, bookedSlots)
        binding.rvSlots.scheduleLayoutAnimation()
    }

    private fun setupObservers() {
        viewModel.bookingStatus.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                binding.inProgress.visibility = View.GONE
                val selectedSlotTime = viewModel.selectedSlot ?: ""
                val dialog = BookingConfirmationDialogFragment.newInstance(selectedSlotTime)
                dialog.show(parentFragmentManager, "BookingConfirmationDialog")

                findNavController().navigate(ManageBookingsFragmentDirections.actionManageBookingsFragmentToDoctorHomeFragment(""))
                viewModel.clearStatus()
            }
        }

        viewModel.bookingError.observe(viewLifecycleOwner) { error ->
            error?.let {
                if (it == "You already have a booking on this date.") {
                    binding.btnConfirm.isEnabled = false
                    Toast.makeText(requireContext(), "لا يمكنك حجز اكثر من ميعاد في نفس التاريخ. ⚠️", Toast.LENGTH_SHORT).show()
                    binding.inProgress.visibility = View.GONE
                } else {
                    binding.btnConfirm.isEnabled = true
                    Toast.makeText(requireContext(), "Booking Error: $it ❌", Toast.LENGTH_SHORT).show()
                    binding.inProgress.visibility = View.GONE
                }
                viewModel.clearStatus()
            }
        }

        viewModel.availableSlots.observe(viewLifecycleOwner) { availableSlots ->
            val currentDisplayedSlots = when {
                binding.morningSlots.backgroundTintList?.defaultColor == resources.getColor(R.color.default_pink_transparent, null) -> morningSlotsData
                binding.eveningSlots.backgroundTintList?.defaultColor == resources.getColor(R.color.default_pink_transparent, null) -> eveningSlotsData
                else -> emptyList()
            }
            val bookedSlots = viewModel.bookedSlots.value ?: emptyList()
            slotAdapter.updateSlots(currentDisplayedSlots, bookedSlots)
            binding.rvSlots.scheduleLayoutAnimation()
        }

        viewModel.bookedSlots.observe(viewLifecycleOwner) { bookedSlots ->
            val currentDisplayedSlots = when {
                binding.morningSlots.backgroundTintList?.defaultColor == resources.getColor(R.color.default_pink_transparent, null) -> morningSlotsData
                binding.eveningSlots.backgroundTintList?.defaultColor == resources.getColor(R.color.default_pink_transparent, null) -> eveningSlotsData
                else -> emptyList()
            }
            slotAdapter.updateSlots(currentDisplayedSlots, bookedSlots)
            binding.rvSlots.scheduleLayoutAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}