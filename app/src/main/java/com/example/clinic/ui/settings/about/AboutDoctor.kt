package com.example.clinic.ui.settings.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.FragmentBAboutDoctorBinding


class AboutDoctor: Fragment() {
    private var _binding: FragmentBAboutDoctorBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_b_about_doctor , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBAboutDoctorBinding.bind(view)
        backArrowClick()
        bookClick()
        navigateClick()
    }

    private fun backArrowClick() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bookClick(){
        binding.book.setOnClickListener {
            findNavController().navigate(R.id.action_aboutDoctor_to_dateSelectionFragment)
        }
    }

    private fun navigateClick(){
        // For Clinido link
        binding.clindo.setOnClickListener {
            val clinidoUrl = "https://clinido.com/ar/drprofile/66083/5496"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(clinidoUrl))
            startActivity(intent)
        }

        binding.clindoLogo.setOnClickListener {
            val clinidoUrl = "https://clinido.com/ar/drprofile/66083/5496"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(clinidoUrl))
            startActivity(intent)
        }

        binding.veseeta.setOnClickListener {
            val vezeetaUrl = "https://app.vezeeta.com/kgiF1tVmBu7EJivX9"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(vezeetaUrl))
            startActivity(intent)
        }

        binding.veseetaLogo.setOnClickListener {
            val vezeetaUrl = "https://app.vezeeta.com/kgiF1tVmBu7EJivX9"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(vezeetaUrl))
            startActivity(intent)
        }

    }
}