package com.example.clinic.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clinic.R
import com.example.clinic.data.DoctorBooking
import com.example.clinic.databinding.ItemBookedDoctorBinding

class DoctorBookingAdapter(private val bookings: MutableList<DoctorBooking>, private val onUpdateState: (DoctorBooking, String) -> Unit) : RecyclerView.Adapter<DoctorBookingAdapter.DoctorBookingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorBookingViewHolder {
        val binding = ItemBookedDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorBookingViewHolder(binding, onUpdateState) { position, newState ->
            updateBookingFinalState(position, newState)
        }
    }

    override fun onBindViewHolder(holder:DoctorBookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.bind(booking)
    }

    override fun getItemCount(): Int = bookings.size

    fun updateBookingFinalState(position: Int, newState: String) {
        bookings[position].finalState = newState
        notifyItemChanged(position)
    }

    class DoctorBookingViewHolder(
        private val binding: ItemBookedDoctorBinding,
        private val onUpdateState: (DoctorBooking, String) -> Unit,
        private val onLocalUpdate: (Int, String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(booking: DoctorBooking) {
            binding.nameText.text = booking.patientName
            binding.phoneText.text = booking.patientPhone
            binding.date.text = booking.date
            binding.slot.text = booking.slot

            if (!booking.note.isNullOrEmpty()) {
                binding.note.visibility = View.VISIBLE
                binding.noteText.text = "${booking.note}"
            } else {
                binding.noteText.text = "لا يوجد"
            }

            if (booking.finalState == "") {
                binding.buttonsLayout.visibility = View.VISIBLE
                binding.finalState.visibility = View.GONE
            } else if (booking.finalState == "attended") {
                binding.buttonsLayout.visibility = View.GONE
                binding.finalState.visibility = View.VISIBLE
                binding.finalState.setImageResource(R.drawable.done)
            } else if (booking.finalState == "not attended") {
                binding.buttonsLayout.visibility = View.GONE
                binding.finalState.visibility = View.VISIBLE
                binding.finalState.setImageResource(R.drawable.cancel)
            }

            binding.btnAttended.setOnClickListener {
                onLocalUpdate(adapterPosition, "attended")      // update list + notify
                onUpdateState(booking, "attended")             // update Firebase
            }

            binding.btnNotAttended.setOnClickListener {
                onLocalUpdate(adapterPosition, "not attended")
                onUpdateState(booking, "not attended")
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateBookings(newBookings: List<DoctorBooking>) {
        newBookings.forEach { newItem ->
            val local = bookings.find { it.bookingId == newItem.bookingId }
            if (local?.finalState != null) {
                newItem.finalState = local.finalState
            }
        }
        bookings.clear()
        bookings.addAll(newBookings)
        notifyDataSetChanged()
    }
}