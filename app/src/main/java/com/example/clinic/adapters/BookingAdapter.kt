package com.example.clinic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clinic.R
import com.example.clinic.data.Booking

class BookingAdapter(private val bookings: MutableList<Booking>, private val onDelete: (String) -> Unit) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booked, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.bind(booking, onDelete)
    }

    override fun getItemCount(): Int = bookings.size

    fun removeItem(bookingId: String) {
        val position = bookings.indexOfFirst { it.id == bookingId }
        if (position != -1) {
            bookings.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.date)
        private val tvSlot: TextView = itemView.findViewById(R.id.slot)
        private val btnDelete: TextView = itemView.findViewById(R.id.delete)

        fun bind(booking: Booking, onDelete: (String) -> Unit) {
            tvDate.text = "Date: ${booking.date}"
            tvSlot.text = "Slot: ${booking.timeSlot}"

            if (booking.isDeletable) {
                btnDelete.visibility = View.VISIBLE
                btnDelete.isEnabled = true
                btnDelete.setOnClickListener {
                    onDelete(booking.id)
                }
            } else {
                btnDelete.visibility = View.GONE
                btnDelete.setOnClickListener(null)
            }
        }
    }
}