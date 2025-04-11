package com.example.clinic.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.clinic.R

class SlotsAdapter(private var slots: List<String>, private val onSlotSelected: (String) -> Unit) : RecyclerView.Adapter<SlotsAdapter.SlotViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class SlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSlot: TextView = itemView.findViewById(R.id.tv_slot)
        val cardSlot: CardView = itemView.findViewById(R.id.card_slot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slot, parent, false)
        return SlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        val slot = slots[position]
        holder.tvSlot.text = slot

        if (selectedPosition == position) {
            holder.cardSlot.setCardBackgroundColor(Color.parseColor("#D8D8D8"))
        } else {
            holder.cardSlot.setCardBackgroundColor(Color.WHITE)
        }

        holder.itemView.setOnClickListener {
            selectedPosition = holder.adapterPosition
            onSlotSelected(slot)
            notifyDataSetChanged()
        }
    }

    fun updateSlots(newSlots: List<String>) {
        this.slots = newSlots
        selectedPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = slots.size
}