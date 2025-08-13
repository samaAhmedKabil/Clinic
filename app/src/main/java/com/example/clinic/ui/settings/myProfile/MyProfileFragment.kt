package com.example.clinic.ui.settings.myProfile

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
import com.example.clinic.databinding.FragmentBMyProfileBinding
import com.example.clinic.repos.UserProfileRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MyProfileFragment: Fragment() {
    private var _binding: FragmentBMyProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyProfileViewModel by viewModels { MyProfileViewModel.MyProfileViewModelFactory(
        UserProfileRepo(FirebaseDatabase.getInstance())) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_b_my_profile , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBMyProfileBinding.bind(view)
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModel.loadUserProfile(userId)
        binding.inProgress.visibility = View.VISIBLE
        backArrowClick()
        observeData()

        setupEditButton(binding.phoneText, binding.editPhone, ::savePhone)
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
            binding.specializationText.setText(it.role)
            binding.inProgress.visibility = View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        viewModel.updateSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                binding.inProgress.visibility = View.GONE
                Toast.makeText(requireContext(), "" +
                        "تم تعديل الملف الشخصي بنجاح", Toast.LENGTH_SHORT).show()
            } else {
                binding.inProgress.visibility = View.GONE
                Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupEditButton(editText: EditText, editButton: ImageButton, saveAction: (String) -> Unit) {
        editButton.setOnClickListener {
            if (editText.isEnabled) { // Currently editable -> try to save
                val newValue = editText.text.toString().trim()
                if (newValue.isEmpty()) {
                    Toast.makeText(requireContext(), "يجب ادخال التعديل", Toast.LENGTH_SHORT).show()
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
    private fun savePhone(newPhone: String) {
        binding.inProgress.visibility = View.VISIBLE
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val currentProfile = viewModel.userProfile.value
        if (currentProfile == null) {
            Toast.makeText(requireContext(), "لم يتم تحميل ملفك الشخصي", Toast.LENGTH_SHORT).show()
            return
        }

        // Basic phone number validation (add more robust regex if needed)
        if (newPhone.length < 8 || newPhone.any { !it.isDigit() }) {
            Toast.makeText(requireContext(), "من فضلك ادخل رقم التيليفون من 11 رقم", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedUser = currentProfile.copy(phone = newPhone)
        viewModel.updateUserProfile(userId, updatedUser)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}