package com.example.clinic.ui.patient.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.FragmentPatientHome2Binding
import com.example.clinic.ui.dialogs.ConfirmQuitDialog
import com.example.clinic.utils.SharedPrefManager

class HomeFragment:Fragment() {
    private var _binding: FragmentPatientHome2Binding? = null
    private val binding get() = _binding!!
    private var userName: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_patient_home_2 , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPatientHome2Binding.bind(view)
        val sharedPrefManager = SharedPrefManager(requireContext())
        userName = sharedPrefManager.getUserName()
        binding.type.text = userName

        onBackPressed()

        binding.bookNow.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSlotSelectionFragment())
        }
        binding.myBookings.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToBookedAppointmentsFragment())
        }
        binding.commonQues.setOnClickListener {
            //findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
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