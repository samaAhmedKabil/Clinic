package com.example.clinic.ui.patient.Home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.FragmentPatientHome2Binding
import com.example.clinic.ui.dialogs.ConfirmQuitDialog
import com.example.clinic.utils.SharedPrefManager

class HomeFragment:Fragment() {
    private var _binding: FragmentPatientHome2Binding? = null
    private val binding get() = _binding!!
    private var userName: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_patient_home_2 , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPatientHome2Binding.bind(view)
        val sharedPrefManager = SharedPrefManager(requireContext())
        userName = sharedPrefManager.getUserName()
        binding.type.text = userName

        onBackPressed()
        addressClick()
        whatsAppClick()

        binding.bookNow.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDateSelectionFragment())
        }
        binding.myBookings.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToBookedAppointmentsFragment())
        }
        binding.commonQues.setOnClickListener {
            //findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
        }
        binding.menu.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
        }
    }

    private fun addressClick() {
        binding.location.setOnClickListener {
            openGoogleMaps()
        }
    }

    private fun openGoogleMaps() {
        val latitude = 30.002160517828127
        val longitude = 31.170703790458393
        val label = "Dr. EHAB KABIL Clinic"

        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($label)")

        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps") // Specify package to ensure Google Maps app is used

        if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(mapIntent)
        } else {
            val webMapUrl = "https://maps.google.com/?q=$latitude,$longitude"
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webMapUrl))
            startActivity(webIntent)
        }
    }

    private fun onBackPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner , object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val dialog = ConfirmQuitDialog()
                dialog.show(parentFragmentManager, "ConfirmDialog")
            }
        })
    }

    private fun whatsAppClick(){
        binding.WhatsApp.setOnClickListener {
            openWhatsAppChat()
        }
    }

    private fun openWhatsAppChat() {
        val phoneNumber = "+201102977959" // International format, no spaces or dashes
        val url = "https://wa.me/${phoneNumber.replace("+", "")}"

        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            intent.setPackage("com.whatsapp")
            startActivity(intent)
        } catch (e: Exception) {
            // WhatsApp not installed or error — open in browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}