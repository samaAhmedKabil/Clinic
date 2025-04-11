package com.example.clinic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.clinic.R
import com.example.clinic.data.Patient

class AllCasesAdapter(private val onPatientClick: (Patient) -> Unit) : ListAdapter<Patient, AllCasesAdapter.AllCasesViewHolder>(
    PatientDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCasesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_case, parent, false)
        return AllCasesViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllCasesViewHolder, position: Int) {
        val patient = getItem(position)
        holder.bind(patient)
    }

    inner class AllCasesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.name)
        private val tvPhone: TextView = itemView.findViewById(R.id.phone)

        fun bind(patient: Patient) {
            tvName.text = patient.name
            tvPhone.text = patient.phone

            itemView.setOnClickListener {
                onPatientClick(patient)
            }
        }
    }
}

class PatientDiffCallback : DiffUtil.ItemCallback<Patient>() {
    override fun areItemsTheSame(oldItem: Patient, newItem: Patient): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Patient, newItem: Patient): Boolean {
        return oldItem == newItem
    }
}