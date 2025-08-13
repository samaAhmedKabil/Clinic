package com.example.clinic.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clinic.databinding.ItemImgBinding

class DoctorServicesAdapter(
    private val items: List<Int>
) : RecyclerView.Adapter<DoctorServicesAdapter.DoctorServiceViewHolder>() {

    inner class DoctorServiceViewHolder(val binding: ItemImgBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorServiceViewHolder {
        val binding = ItemImgBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DoctorServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorServiceViewHolder, position: Int) {
        val imageRes = items[position]
        holder.binding.img.setImageResource(imageRes)
    }

    override fun getItemCount(): Int = items.size
}
