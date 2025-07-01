package com.example.clinic.ui.auth

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.utils.ConstData
import com.example.clinic.R
import com.example.clinic.databinding.FragmentBRoleSelectionBinding

class RoleSelectionFragment: Fragment() {
    private var _binding: FragmentBRoleSelectionBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_b_role_selection , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBRoleSelectionBinding.bind(view)
        checkBoxClick()
        nextClick()
    }
    private fun checkBoxClick() {
        binding.patientCheckBox.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.doctorCheckBox.isChecked = !b
                ConstData.userType = ConstData.PATIENT_TYPE
                binding.patientCheckBox.buttonTintList = ColorStateList.valueOf(requireContext().getColor(R.color.default_pink))
            } else {
                ConstData.userType = ""
            }
        }

        binding.doctorCheckBox.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.patientCheckBox.isChecked = !b
                ConstData.userType = ConstData.DOCTOR_TYPE
                binding.doctorCheckBox.buttonTintList = ColorStateList.valueOf(requireContext().getColor(R.color.default_pink))
            } else {
                ConstData.userType = ""
            }
        }
    }

    private fun nextClick() {
        binding.btnNext.setOnClickListener {
            if (binding.patientCheckBox.isChecked || binding.doctorCheckBox.isChecked) {
                findNavController().navigate(RoleSelectionFragmentDirections.actionRoleSelectionFragmentToLoginFragment(
                    ConstData.userType))
            } else {
                Toast.makeText(requireContext(), getString(R.string.noUserTypeSelected), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}