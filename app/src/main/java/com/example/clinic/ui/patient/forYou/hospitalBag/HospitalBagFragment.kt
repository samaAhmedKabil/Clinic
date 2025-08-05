package com.example.clinic.ui.patient.forYou.hospitalBag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic.R
import com.example.clinic.adapters.HospitalBagAdapter
import com.example.clinic.data.HospitalBagItem
import com.example.clinic.databinding.FragmentPatientHospitalBagBinding

class HospitalBagFragment: Fragment() {
    private var _binding: FragmentPatientHospitalBagBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientHospitalBagBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrowBackClick()
        setupHospitalBagLists()
    }

    private fun arrowBackClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupHospitalBagLists() {
        val momNeeds = listOf(
            HospitalBagItem("بيجامة", R.drawable.caring_pregnant),
            HospitalBagItem("ملابس", R.drawable.caring_pregnant),
            HospitalBagItem("شباشب", R.drawable.caring_pregnant),
            HospitalBagItem("فوط صحية لما بعد الولادة", R.drawable.caring_pregnant),
            HospitalBagItem("ملابس داخلية", R.drawable.caring_pregnant),
            HospitalBagItem("وسادة الرضاعة", R.drawable.caring_pregnant),
            HospitalBagItem("غطاء الرضاعة", R.drawable.caring_pregnant),
            HospitalBagItem("فرشاة اسنان", R.drawable.caring_pregnant),
            HospitalBagItem("معجون اسنان", R.drawable.caring_pregnant),
            HospitalBagItem("شامبو", R.drawable.caring_pregnant),
            HospitalBagItem("جل الاستحمام", R.drawable.caring_pregnant)
        )

        val babyNeeds = listOf(
            HospitalBagItem("حفاضات", R.drawable.baby_image),
            HospitalBagItem("كريم التهابات", R.drawable.baby_image),
            HospitalBagItem("مناديل مبللة", R.drawable.baby_image),
            HospitalBagItem("زيت اطفال", R.drawable.baby_image),
            HospitalBagItem("بطانية", R.drawable.baby_image),
            HospitalBagItem("فوطة", R.drawable.baby_image),
            HospitalBagItem("شرابات", R.drawable.baby_image),
            HospitalBagItem("قفازات", R.drawable.baby_image),
            HospitalBagItem("ملابس للنوم", R.drawable.baby_image),
            HospitalBagItem("شامبو للاطفال", R.drawable.baby_image),
            HospitalBagItem("ملابس داخلية للاطفال", R.drawable.baby_image)
        )

        val momAdapter = HospitalBagAdapter(momNeeds)
        val babyAdapter = HospitalBagAdapter(babyNeeds)

        binding.rvMomNeeds.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = momAdapter
        }

        binding.rvBabyNeeds.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = babyAdapter
        }
    }

    }