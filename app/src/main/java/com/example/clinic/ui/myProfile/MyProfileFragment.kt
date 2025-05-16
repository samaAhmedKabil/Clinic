package com.example.clinic.ui.myProfile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }

    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeData(){
        viewModel.userProfile.observe(viewLifecycleOwner) {
            binding.name.text = it.fname + " " + it.lname
            binding.emailText.text = it.email
            binding.phoneText.text = it.phone
            binding.ageText.text = it.age.toString()
            binding.locationText.text = it.address
            binding.specializationText.text = it.role
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }
}