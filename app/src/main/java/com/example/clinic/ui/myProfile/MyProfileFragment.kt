package com.example.clinic.ui.myProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.FeagmentMyProfileBinding
import com.example.clinic.repos.UserProfileRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MyProfileFragment: Fragment() {
    private var _binding: FeagmentMyProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyProfileViewModel by viewModels { MyProfileViewModel.MyProfileViewModelFactory(
        UserProfileRepo(FirebaseDatabase.getInstance())) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.feagment_my_profile , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FeagmentMyProfileBinding.bind(view)
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModel.loadUserProfile(userId)
        backArrowClick()
        observeData()

        setupEditButton(binding.ageText, binding.editAge, ::saveAge)
        setupEditButton(binding.phoneText, binding.editPhone, ::savePhone)
        setupEditButton(binding.locationText, binding.editLocation, ::saveLocation)
    }

    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeData(){
        viewModel.userProfile.observe(viewLifecycleOwner) {
            binding.name.text = it.fname + " " + it.lname
            binding.emailText.setText(it.email)
            binding.phoneText.setText(it.phone)
            binding.ageText.setText(it.age.toString())
            binding.locationText.setText(it.address)
            binding.specializationText.setText(it.role)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        viewModel.updateSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupEditButton(editText: EditText, editButton: ImageButton, saveAction: (String) -> Unit) {
        editButton.setOnClickListener {
            if (editText.isEnabled) { // Currently editable -> try to save
                val newValue = editText.text.toString().trim()
                if (newValue.isEmpty()) {
                    Toast.makeText(requireContext(), "Field cannot be empty.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                saveAction(newValue) // Call the specific save function
                toggleEditTextState(editText, editButton, false)
            } else { // Not editable -> enter edit mode
                toggleEditTextState(editText, editButton, true)
            }
        }
    }

    private fun toggleEditTextState(editText: EditText, editButton: ImageButton, enable: Boolean) {
        editText.isEnabled = enable
        editText.isFocusable = enable
        editText.isFocusableInTouchMode = enable
        editText.isCursorVisible = enable
        editText.isClickable = enable

        if (enable) {
            editButton.setImageResource(R.drawable.done) // Change to save icon
            editText.requestFocus()
            editText.setSelection(editText.text?.length ?: 0) // Place cursor at end
        } else {
            editButton.setImageResource(R.drawable.pencil) // Change back to edit icon
            editText.clearFocus()
        }
    }

    // --- Specific Save Functions ---
    private fun saveAge(newAgeString: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val currentProfile = viewModel.userProfile.value
        if (currentProfile == null) {
            Toast.makeText(requireContext(), "User profile data not loaded.", Toast.LENGTH_SHORT).show()
            return
        }

        val age = newAgeString.toIntOrNull()
        if (age == null || age <= 0 || age > 150) { // Basic age validation
            Toast.makeText(requireContext(), "Please enter a valid age.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedUser = currentProfile.copy(age = age)
        viewModel.updateUserProfile(userId, updatedUser)
    }

    private fun savePhone(newPhone: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val currentProfile = viewModel.userProfile.value
        if (currentProfile == null) {
            Toast.makeText(requireContext(), "User profile data not loaded.", Toast.LENGTH_SHORT).show()
            return
        }

        // Basic phone number validation (add more robust regex if needed)
        if (newPhone.length < 8 || newPhone.any { !it.isDigit() }) {
            Toast.makeText(requireContext(), "Please enter a valid phone number.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedUser = currentProfile.copy(phone = newPhone)
        viewModel.updateUserProfile(userId, updatedUser)
    }

    private fun saveLocation(newAddress: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val currentProfile = viewModel.userProfile.value
        if (currentProfile == null) {
            Toast.makeText(requireContext(), "User profile data not loaded.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedUser = currentProfile.copy(address = newAddress)
        viewModel.updateUserProfile(userId, updatedUser)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}