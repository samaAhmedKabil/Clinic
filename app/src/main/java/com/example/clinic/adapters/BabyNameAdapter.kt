package com.example.clinic.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clinic.R
import com.example.clinic.data.BabyName
import com.example.clinic.databinding.ItemBabyNameBinding

class BabyNameAdapter(
    private var names: List<BabyName>
) : RecyclerView.Adapter<BabyNameAdapter.BabyNameViewHolder>() {

    inner class BabyNameViewHolder(val binding: ItemBabyNameBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyNameViewHolder {
        val binding =
            ItemBabyNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BabyNameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BabyNameViewHolder, position: Int) {
        val item = names[position]
        holder.binding.name.text = item.name
        if (item.gender.equals("boy", true)) {
            holder.binding.img.setBackgroundResource(R.drawable.baby_boy)
        } else {
            holder.binding.img.setBackgroundResource(R.drawable.baby_girl)
        }
    }

    override fun getItemCount() = names.size

    fun updateList(newList: List<BabyName>) {
        names = newList
        notifyDataSetChanged()
    }
}