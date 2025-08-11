package com.example.clinic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clinic.R
import com.example.clinic.data.Service
import com.example.clinic.databinding.ItemPricesBinding

class ServicesAdapter(
    private var services: MutableList<Service>,
    private var isDoctor: Boolean,
    private val onEditClick: (Service) -> Unit
) : RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>() {

    inner class ServiceViewHolder(val binding: ItemPricesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: Service) {
            binding.serviceName1.text = service.name
            binding.servicePrice1.text = service.price.toString()

            if (isDoctor) {
                binding.buttonEdit1.visibility = View.VISIBLE
                binding.buttonEdit1.setOnClickListener {
                    onEditClick(service)
                }
            } else {
                binding.buttonEdit1.setImageResource(R.drawable.age)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemPricesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(services[position])
    }

    override fun getItemCount(): Int = services.size

    fun updateList(newList: List<Service>) {
        services.clear()
        services.addAll(newList)
        notifyDataSetChanged()
    }

}
