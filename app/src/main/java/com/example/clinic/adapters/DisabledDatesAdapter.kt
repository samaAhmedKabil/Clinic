package com.example.clinic.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clinic.data.DisabledDate
import com.example.clinic.databinding.ItemManageDatesBinding

class DisabledDatesAdapter(
    private var dates: List<DisabledDate>,
    private val onDelete: (DisabledDate) -> Unit
) : RecyclerView.Adapter<DisabledDatesAdapter.DateViewHolder>() {

    inner class DateViewHolder(val binding: ItemManageDatesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = ItemManageDatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun getItemCount(): Int = dates.size

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val date = dates[position]
        holder.binding.year.setText(date.year.toString())
        holder.binding.month.setText(date.month.toString())
        holder.binding.day.setText(date.day.toString())
        holder.binding.delete.setOnClickListener {
            onDelete(date)
        }
    }

    fun updateList(newDates: List<DisabledDate>) {
        dates = newDates
        notifyDataSetChanged()
    }
}