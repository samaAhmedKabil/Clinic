package com.example.clinic.ui.doctor.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.FragmentDoctorHomeBinding
import com.example.clinic.ui.dialogs.ConfirmCancelingDialog
import com.example.clinic.ui.dialogs.ConfirmQuitDialog
import com.example.clinic.utils.SharedPrefManager

class DoctorHomeFragment :Fragment() {
    private var _binding: FragmentDoctorHomeBinding? = null
    private val binding get() = _binding!!
    private var userName: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctor_home , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDoctorHomeBinding.bind(view)
        val sharedPrefManager = SharedPrefManager(requireContext())
        userName = sharedPrefManager.getUserName()
        binding.type.text = userName

        onBackPressed()

        binding.blue.setOnClickListener {
            findNavController().navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToAllCasesFragment())
        }
        binding.purple.setOnClickListener {
            findNavController().navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToAllBookingsFragment())
        }
        binding.settings.setOnClickListener {
            findNavController().navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToSettingsFragment())
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