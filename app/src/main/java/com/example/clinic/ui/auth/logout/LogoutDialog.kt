package com.example.clinic.ui.auth.logout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.clinic.databinding.DialogLogoutBinding
import com.example.clinic.ui.settings.SettingsFragmentDirections
import com.example.clinic.utils.SharedPrefManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth

class LogoutDialog: BottomSheetDialogFragment() {

    private var _binding: DialogLogoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogLogoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ok.setOnClickListener {
            val sharedPrefManager = SharedPrefManager(requireContext())
            sharedPrefManager.clearPrefs()

            // 2. Sign out from FirebaseAuth if you're using it
            FirebaseAuth.getInstance().signOut()

            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToRoleSelectionFragment())
            dismiss()
        }
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }
}