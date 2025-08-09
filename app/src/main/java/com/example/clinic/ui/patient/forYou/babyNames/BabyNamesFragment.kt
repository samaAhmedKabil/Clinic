package com.example.clinic.ui.patient.forYou.babyNames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic.R
import com.example.clinic.adapters.BabyNameAdapter
import com.example.clinic.databinding.FragmentPatientBabyNamesBinding
import com.example.clinic.ui.dialogs.AddBabyNameDialog
import com.google.android.material.button.MaterialButton

class BabyNamesFragment : Fragment() {

    private var _binding: FragmentPatientBabyNamesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BabyNameViewModel by viewModels()
    private lateinit var adapter: BabyNameAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientBabyNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = BabyNameAdapter(emptyList())
        binding.rvNames.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNames.adapter = adapter

        arrowBackClick()
        updateButtonBackground(binding.morningSlots)

        viewModel.babyNames.observe(viewLifecycleOwner) { names ->
            adapter.updateList(names)
        }

        binding.add.setOnClickListener {
            AddBabyNameDialog { name, gender ->
                viewModel.addBabyName(name, gender)
            }.show(parentFragmentManager, "AddBabyNameDialog")
        }

        binding.eveningSlots.setOnClickListener {
            updateButtonBackground(binding.eveningSlots)
            viewModel.filterByGender("girl")
        }

        binding.morningSlots.setOnClickListener {
            updateButtonBackground(binding.morningSlots)
            viewModel.filterByGender("boy")
        }
    }

    private fun arrowBackClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun updateButtonBackground(clickedButton: MaterialButton) {
        binding.morningSlots.setBackgroundTintList(resources.getColorStateList(R.color.light_grey, null))
        binding.eveningSlots.setBackgroundTintList(resources.getColorStateList(R.color.light_grey, null))

        clickedButton.setBackgroundTintList(resources.getColorStateList(R.color.default_pink_transparent, null))
    }
}
