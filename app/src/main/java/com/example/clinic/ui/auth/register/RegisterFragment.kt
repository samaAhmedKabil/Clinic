package com.example.clinic.ui.auth.register

import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.clinic.utils.ConstData
import com.example.clinic.R
import com.example.clinic.databinding.FragmentRegisterBinding
import com.example.clinic.ui.auth.viewModel.AuthViewModel
import com.example.clinic.utils.SharedPrefManager


class RegisterFragment: Fragment() {
    private var _binding: FragmentRegisterBinding?= null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    private var userType: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterBinding.bind(view)
        userType = RegisterFragmentArgs.fromBundle(requireArguments()).userType
        registerClick()
        showPassword()
    }
    private fun registerClick(){
        binding.btnRegister.setOnClickListener{
            validate()
        }
    }
    private fun showPassword(){
        binding.showPass.setOnClickListener {
            if (binding.password.inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                binding.password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.password.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }
    private fun validate(){
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val fName = binding.firstName.text.toString().trim()
        val lName = binding.lastName.text.toString().trim()
        val age = binding.age.text.toString().trim()
        val phone = binding.phone.text.toString().trim()
        val address = binding.address.text.toString().trim()
        if (email.isEmpty()){
            binding.email.error = getString(R.string.required)
        }
        else if (password.isEmpty()){
            binding.password.error = getString(R.string.required)
        }
        else if (fName.isEmpty()){
            binding.firstName.error = getString(R.string.required)
        }
        else if (lName.isEmpty()){
            binding.lastName.error = getString(R.string.required)
        }
        else if (age.isEmpty()) {
            binding.age.error = getString(R.string.required)
        }
        else if (phone.isEmpty()){
            binding.phone.error = getString(R.string.required)
        }
        else if (address.isEmpty()) {
            binding.address.error = getString(R.string.required)
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.email.error = getString(R.string.wrong_email)
        }
        else{
            val fullName = "$fName $lName"
            val sharedPrefManager = SharedPrefManager(requireContext())
            sharedPrefManager.saveUserName(fullName)
            authViewModel.register(email, password, userType, fName, lName, age.toInt(), phone, address) { success ->
                if (success) {
                    Toast.makeText(context, "Register Successful", Toast.LENGTH_SHORT).show()
                    if(userType == ConstData.DOCTOR_TYPE){
                        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToDoctorHomeFragment(fullName))
                    }
                    if(userType == ConstData.PATIENT_TYPE){
                        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToHomeFragment(fullName))
                    }
                } else {
                    Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}