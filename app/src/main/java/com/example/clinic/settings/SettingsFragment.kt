package com.example.clinic.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.FragmentSettingsBinding
import com.example.clinic.utils.SharedPrefManager
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment: Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)
        logoutClick()
        backArrowClick()
        myProfileClick()
    }
    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun logoutClick(){
        binding.logout.setOnClickListener {
            val sharedPrefManager = SharedPrefManager(requireContext())
            sharedPrefManager.clearPrefs()

            // 2. Sign out from FirebaseAuth if you're using it
            FirebaseAuth.getInstance().signOut()

            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToRoleSelectionFragment())
        }
    }
    private fun myProfileClick(){
        //
    }
}