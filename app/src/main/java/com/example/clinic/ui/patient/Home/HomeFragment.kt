package com.example.clinic.ui.patient.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.FragmentHomeBinding
import com.example.clinic.ui.dialogs.ConfirmQuitDialog
import com.example.clinic.utils.SharedPrefManager
import com.google.firebase.auth.FirebaseAuth

class HomeFragment:Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var userName: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        val sharedPrefManager = SharedPrefManager(requireContext())
        userName = sharedPrefManager.getUserName()
        binding.type.text = userName

        onBackPressed()

        binding.blue.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSlotSelectionFragment())
        }
        binding.purple.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToBookedAppointmentsFragment())
        }
        binding.settings.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
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