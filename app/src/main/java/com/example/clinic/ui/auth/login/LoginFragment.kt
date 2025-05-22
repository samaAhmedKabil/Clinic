package com.example.clinic.ui.auth.login

import android.content.Context
import android.content.res.ColorStateList
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
import com.example.clinic.databinding.FragmentLoginBinding
import com.example.clinic.ui.auth.viewModel.AuthViewModel
import com.example.clinic.utils.SharedPrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var sharedPrefManager: SharedPrefManager
    private var userType: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        sharedPrefManager = SharedPrefManager(requireContext())
        userType = LoginFragmentArgs.fromBundle(requireArguments()).userType
        loginClick()
        showPassword()
        registerClick()
        rememberClick()
        checkRememberedState()
    }

    private fun loginClick() {
        binding.btnLogin.setOnClickListener {
            validate()
        }
    }

    private fun registerClick() {
        binding.register.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment(ConstData.userType)
            )
        }
    }

    private fun rememberClick() {
        binding.rememberMe.setOnClickListener {
            val isChecked = binding.rememberMe.isChecked
            if (isChecked) {
                binding.rememberMe.buttonTintList =
                    ColorStateList.valueOf(requireContext().getColor(R.color.default_pink))
            }

            sharedPrefManager.setRememberMe(isChecked)
        }
    }

    private fun checkRememberedState() {
        val remembered = sharedPrefManager.isRememberMe()
        binding.rememberMe.isChecked = remembered

        if (remembered) {
            binding.rememberMe.buttonTintList =
                ColorStateList.valueOf(requireContext().getColor(R.color.default_pink))

            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                // Fetch the user name from Firebase for remembered state
                val userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.uid)
                userRef.get().addOnSuccessListener { snapshot ->
                    val fName = snapshot.child("fname").getValue(String::class.java) ?: "Unknown"
                    sharedPrefManager.saveUserName(fName)
                    val savedName = sharedPrefManager.getUserName()
                    if (userType == ConstData.DOCTOR_TYPE) {
                        findNavController().navigate(
                            LoginFragmentDirections.actionLoginFragmentToDoctorHomeFragment(savedName)
                        )
                    } else if (userType == ConstData.PATIENT_TYPE) {
                        findNavController().navigate(
                            LoginFragmentDirections.actionLoginFragmentToHomeFragment(savedName)
                        )
                    }
                }
            }
        }
    }

    private fun showPassword() {
        binding.showPass.setOnClickListener {
            if (binding.editText2.inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                binding.editText2.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.editText2.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }

    private fun validate() {
        val email = binding.editText.text.toString().trim()
        val password = binding.editText2.text.toString().trim()
        if (email.isEmpty()) {
            binding.editText.error = getString(R.string.required)
        } else if (password.isEmpty()) {
            binding.editText2.error = getString(R.string.required)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editText.error = getString(R.string.wrong_email)
        } else {
            authViewModel.login(email, password) { success ->
                if (success) {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()

                    val userId = FirebaseAuth.getInstance().currentUser?.uid

                    if (userId != null) {
                        // Fetch the user name (fName) from Firebase
                        val userRef =
                            FirebaseDatabase.getInstance().getReference("users").child(userId)
                        userRef.get().addOnSuccessListener { snapshot ->
                            val fName = snapshot.child("fname").getValue(String::class.java) ?: "Unknown"
                            sharedPrefManager.saveUserName(fName)
                            // Retrieve the saved name from SharedPreferences
                            val savedName = sharedPrefManager.getUserName()
                            if (userType == ConstData.DOCTOR_TYPE) {
                                findNavController().navigate(
                                    LoginFragmentDirections.actionLoginFragmentToDoctorHomeFragment(savedName)
                                )
                                val prefs = context?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                                prefs?.edit()?.putString("role", "Doctor")?.apply()
                            } else if (userType == ConstData.PATIENT_TYPE) {
                                findNavController().navigate(
                                    LoginFragmentDirections.actionLoginFragmentToHomeFragment(savedName)
                                )
                            }
                        }.addOnFailureListener {
                            Toast.makeText(context, "Failed to fetch user info", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}