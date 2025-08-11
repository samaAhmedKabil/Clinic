package com.example.clinic.ui.settings.prices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic.adapters.ServicesAdapter
import com.example.clinic.data.Service
import com.example.clinic.databinding.FragmentDoctorPricesBinding
import com.example.clinic.repos.ServicesRepo
import com.example.clinic.ui.dialogs.ServicesDialog

class ServicesFragment : Fragment() {

    private var _binding: FragmentDoctorPricesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ServicesAdapter
    private lateinit var viewModel: ServicesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorPricesBinding.inflate(inflater, container, false)
        val repository = ServicesRepo()
        val factory = ServicesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ServicesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Initialize adapter with empty list and isDoctor = false for now
        adapter = ServicesAdapter(mutableListOf(), false) { service ->
            // Edit button clicked
            showBottomSheet(service)
        }

        arrowBackClick()

        binding.rvPrices.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPrices.adapter = adapter

        // Observe doctor mode
        viewModel.isDoctor.observe(viewLifecycleOwner) { isDoctor ->
            // Update adapter's doctor mode
            adapter = ServicesAdapter(mutableListOf(), isDoctor) { service ->
                showBottomSheet(service)
            }
            binding.rvPrices.adapter = adapter
            binding.addNewService.visibility = if (isDoctor) View.VISIBLE else View.GONE

            // Re-set services after adapter re-initialization
            viewModel.services.value?.let { adapter.updateList(it) }
        }

        // Observe services list
        viewModel.services.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)
        }

        // Add new service button
        binding.addNewService.setOnClickListener {
            showBottomSheet(null)
        }

        // Load initial data
        viewModel.checkUserRole()
        viewModel.loadServices()
    }

    private fun arrowBackClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showBottomSheet(service: Service?) {
        val bottomSheet = ServicesDialog(service) { newService ->
            viewModel.saveService(newService) // Save to Firebase
        }
        bottomSheet.show(parentFragmentManager, "ServicesBottomSheet")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
