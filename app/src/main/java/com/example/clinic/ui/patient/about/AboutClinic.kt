package com.example.clinic.ui.patient.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.FragmentAboutClinicBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AboutClinic : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentAboutClinicBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_about_clinic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAboutClinicBinding.bind(view)
        backArrowClick()
        bookClick()
        facebookClick()
        instagramClick()
        whatsAppClick()
        addressClick()
        tikTokClick()
        initMap()
    }

    private fun addressClick() {
        binding.address.setOnClickListener {
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

    private fun backArrowClick() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bookClick(){
        binding.book.setOnClickListener {
            findNavController().navigate(R.id.action_aboutClinic_to_dateSelectionFragment)
        }
    }

    private fun facebookClick(){
        binding.facebook.setOnClickListener {
            openFacebookPage()
        }
    }

    private fun openFacebookPage() {
        val facebookPageId = "dr.ehab.kabil"
        val facebookUrl = "https://www.facebook.com/$facebookPageId"
        val facebookAppUri = "fb://page/$facebookPageId"

        try {
            // Try opening in Facebook app
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookAppUri))
            intent.setPackage("com.facebook.android")
            startActivity(intent)
        } catch (e: Exception) {
            // Fallback to browser if app isn't installed
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl))
            startActivity(intent)
        }
    }

    private fun tikTokClick(){
        binding.tikTok.setOnClickListener {
            openTikTokPage()
        }
    }

    private fun openTikTokPage() {
        val tiktokUsername = "dr.ehabkabil"
        val tiktokWebUrl = "https://www.tiktok.com/@$tiktokUsername"
        val tiktokAppUri = Uri.parse("snssdk1233://user/profile/$tiktokUsername") // TikTok's custom URI scheme

        try {
            val intent = Intent(Intent.ACTION_VIEW, tiktokAppUri)
            intent.setPackage("com.ss.android.ugc.trill") // TikTok's package name
            startActivity(intent)
        } catch (e: Exception) {
            // Fallback to browser if TikTok app is not installed
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tiktokWebUrl))
            startActivity(intent)
        }
    }

    private fun instagramClick(){
        binding.Instagram.setOnClickListener {
            openInstagramPage()
        }
    }

    private fun openInstagramPage() {
        val instagramUsername = "dr.ehab.kabil"
        val instagramUrl = "https://www.instagram.com/$instagramUsername"
        val instagramAppUri = Uri.parse("http://instagram.com/_u/$instagramUsername")

        try {
            val intent = Intent(Intent.ACTION_VIEW, instagramAppUri)
            intent.setPackage("com.instagram.android")
            startActivity(intent)
        } catch (e: Exception) {
            // Fallback to browser if Instagram app is not installed
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))
            startActivity(intent)
        }
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

    private fun initMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map_container) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val clinicLocation = LatLng(30.002160517828127, 31.170703790458393)
        googleMap.addMarker(
            MarkerOptions()
                .position(clinicLocation)
                .title("Dr. EHAB KABIL Clinic")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(clinicLocation, 15f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
