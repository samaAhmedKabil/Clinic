package com.example.clinic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clinic.R
import com.example.clinic.data.HospitalBagItem

class HospitalBagAdapter(private val items: List<HospitalBagItem>) :
    RecyclerView.Adapter<HospitalBagAdapter.BagViewHolder>() {

    inner class BagViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.itemName)
        private val image = view.findViewById<ImageView>(R.id.itemImage)

        fun bind(item: HospitalBagItem) {
            name.text = item.name
            image.setImageResource(item.imageRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hospital_bag, parent, false)
        return BagViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BagViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
