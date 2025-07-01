package com.example.clinic.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.FragmentBSettingsBinding
import com.example.clinic.ui.auth.logout.LogoutDialog

class SettingsFragment: Fragment() {
    private var _binding: FragmentBSettingsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_b_settings , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBSettingsBinding.bind(view)
        logoutClick()
        backArrowClick()
        myProfileClick()
        aboutDoctorClick()
        aboutClinicClick()
        feedbacksClick()
    }
    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun logoutClick(){
        binding.logout.setOnClickListener {
            val bottomSheetFragment = LogoutDialog()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }
    }
    private fun myProfileClick(){
        binding.myProfile.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToMyProfileFragment())
        }
    }
    private fun aboutDoctorClick(){
        binding.aboutDoctor.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToAboutDoctor())
        }
    }
    private fun aboutClinicClick(){
        binding.aboutUs.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToAboutClinic())
        }
    }
    private fun feedbacksClick(){
        binding.feedback.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToFeedbacksFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}