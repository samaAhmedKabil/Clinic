package com.example.clinic.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clinic.data.DoctorBooking
import com.example.clinic.databinding.ItemBookedDoctorBinding

class DoctorBookingAdapter(private val bookings: MutableList<DoctorBooking>) : RecyclerView.Adapter<DoctorBookingAdapter.DoctorBookingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorBookingViewHolder {
        val binding = ItemBookedDoctorBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return DoctorBookingViewHolder(binding)

    }

    override fun onBindViewHolder(holder:DoctorBookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.bind(booking)
    }

    override fun getItemCount(): Int = bookings.size

    class DoctorBookingViewHolder(private val binding: ItemBookedDoctorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(booking: DoctorBooking) {
            binding.nameText.text = booking.patientName
            binding.phoneText.text = booking.patientPhone
            binding.date.text = booking.date
            binding.slot.text = booking.slot
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateBookings(newBookings: List<DoctorBooking>) {
        bookings.clear()
        bookings.addAll(newBookings)
        notifyDataSetChanged()
    }
}