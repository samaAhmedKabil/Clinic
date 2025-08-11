package com.example.clinic.ui.doctor.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.FragmentDoctorHome2Binding
import com.example.clinic.ui.dialogs.ConfirmQuitDialog
import com.example.clinic.ui.dialogs.OperationSelectionDialog
import com.example.clinic.ui.dialogs.TellDisabledDialog
import com.example.clinic.ui.patient.Home.HomeFragmentDirections

class DoctorHomeFragment :Fragment() {
    private var _binding: FragmentDoctorHome2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctor_home_2 , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDoctorHome2Binding.bind(view)

        onBackPressed()

        binding.allCases.setOnClickListener {
            findNavController().navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToAllCasesFragment())
        }
        binding.allBookings.setOnClickListener {
            findNavController().navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToAllBookingsFragment())
        }
        binding.commonQues.setOnClickListener {
            findNavController().navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToCommonQuestionsFragment())
        }
        binding.menu.setOnClickListener {
            findNavController().navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToSettingsFragment())
        }
        binding.manageBookings.setOnClickListener {
            val bottomSheetFragment = OperationSelectionDialog()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }
        binding.price.setOnClickListener {
            findNavController().navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToServicesFragment())
        }
    }

    private fun onBackPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner , object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val dialog = ConfirmQuitDialog()
                dialog.show(parentFragmentManager, "ConfirmDialog")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}