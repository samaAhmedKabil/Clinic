package com.example.clinic.ui.doctor.manageBookings

import SlotsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic.adapters.DisabledDatesAdapter
import com.example.clinic.databinding.FragmentDoctorManageBookingsBinding
import com.example.clinic.databinding.FragmentDoctorSetAvailableDatesBinding
import com.example.clinic.repos.BookingRepo
import com.example.clinic.repos.DisabledDatesRepo
import com.example.clinic.ui.dialogs.AddDateDialog
import com.example.clinic.ui.patient.booking.SlotSelectionFragmentArgs
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.getValue

class ManageAvailableDatesFragment: Fragment() {
    private var _binding: FragmentDoctorSetAvailableDatesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DisabledDatesViewModel
    private lateinit var adapter: DisabledDatesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoctorSetAvailableDatesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDoctorSetAvailableDatesBinding.bind(view)

        val repo = DisabledDatesRepo()
        val factory = DisabledDatesViewModel.Factory(repo)
        viewModel = ViewModelProvider(this, factory)[DisabledDatesViewModel::class.java]

        backArrowClick()

        setupRecycler()
        observeViewModel()

        viewModel.loadDisabledDates()

        binding.btnConfirm.setOnClickListener {
            val dialog = AddDateDialog { year, month, day ->
                viewModel.addDisabledDate(year, month, day)
            }
            dialog.show(parentFragmentManager, "AddDateDialog")
        }
    }

    private fun backArrowClick() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecycler() {
        adapter = DisabledDatesAdapter(listOf()) { disabledDate ->
            viewModel.deleteDisabledDate(disabledDate)
        }
        binding.datesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.datesRecycler.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.disabledDates.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)
        }

        viewModel.addStatus.observe(viewLifecycleOwner) { success ->
            success?.let {
                if (it) Toast.makeText(requireContext(), "تم الغاء الحجوزات في هذا التاريخ", Toast.LENGTH_SHORT).show()
                else Toast.makeText(requireContext(), "Invalid input or failed to add", Toast.LENGTH_SHORT).show()
                viewModel.clearStatus()
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}