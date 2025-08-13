package com.example.clinic.ui.auth.forgetPassword

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.FragmentForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordFragment: Fragment() {
    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forget_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentForgetPasswordBinding.bind(view)

        arrowBackClick()


        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "من فضلك ادخل البريد الالكتروني", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simple format check before sending to Firebase
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "البريد الالكتروني غير صحيح", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "تم ارسال بريد الاكتروني لاعادة تعيين كلمة مرور", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), task.exception?.message ?: "Error sending reset email", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun arrowBackClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}