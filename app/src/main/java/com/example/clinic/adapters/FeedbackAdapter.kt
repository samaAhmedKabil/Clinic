package com.example.clinic.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.clinic.R
import com.example.clinic.data.Feedback
import com.example.clinic.databinding.ItemFeedbackBinding

class FeedbackAdapter(
    private val feedbacks: List<Feedback>,
    private val isDoctor: Boolean,
    private val onDelete: (Feedback) -> Unit
) : RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>() {

    inner class FeedbackViewHolder(val binding: ItemFeedbackBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val binding = ItemFeedbackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedbackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val item = feedbacks[position]
        val context = holder.itemView.context

        with(holder.binding) {
            name.text = item.writerName
            feedback.text = item.text

            // Ensure proper color references
            val yellow = ContextCompat.getColor(context, R.color.yellow_star)
            val gray = ContextCompat.getColor(context, R.color.grey)

            val stars = listOf(star1, star2, star3, star4, star5)
            stars.forEachIndexed { index, star ->
                val color = if (index < item.stars) yellow else gray
                ImageViewCompat.setImageTintList(star, ColorStateList.valueOf(color))
            }

            // Show delete button only if the user is a doctor
            delete.visibility = if (isDoctor) View.VISIBLE else View.GONE
            delete.setOnClickListener { onDelete(item) }
        }
    }


    override fun getItemCount() = feedbacks.size
}