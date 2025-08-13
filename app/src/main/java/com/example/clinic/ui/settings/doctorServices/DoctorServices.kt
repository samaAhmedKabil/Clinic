package com.example.clinic.ui.settings.doctorServices

import android.widget.MediaController
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.adapters.DoctorServicesAdapter
import com.example.clinic.databinding.FragmentBDoctorServicesBinding

class DoctorServices : Fragment() {
    private var _binding: FragmentBDoctorServicesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBDoctorServicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backArrowClick()

        val images = listOf(
            R.drawable.clinic_pic_12,
            R.drawable.clinic_pic_1,
            R.drawable.clinic_pic_2,
            R.drawable.clinic_pic_3,
            R.drawable.clinic_pic_4,
            R.drawable.clinic_pic_5,
            R.drawable.clinic_pic_6,
            R.drawable.clinic_pic_7,
            R.drawable.clinic_pic_8,
            R.drawable.clinic_pic_9,
            R.drawable.clinic_pic_10,
            R.drawable.clinic_pic_11
        )

        val adapter = DoctorServicesAdapter(images)
        binding.horizontalRecyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext(),
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        binding.horizontalRecyclerView.adapter = adapter

        // Video from raw folder
        val videoUri =
            Uri.parse("android.resource://${requireContext().packageName}/${R.raw.services_video}")

        // MediaController for playback controls
        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(binding.servicesVideo)

        // Set up VideoView
        binding.servicesVideo.setMediaController(mediaController)
        binding.servicesVideo.setVideoURI(videoUri)
        binding.servicesVideo.requestFocus()

        // Auto start when ready
        binding.servicesVideo.setOnPreparedListener { mp ->
            mp.isLooping = true // ✅ Loop if desired
            binding.servicesVideo.start()
        }
    }

    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}