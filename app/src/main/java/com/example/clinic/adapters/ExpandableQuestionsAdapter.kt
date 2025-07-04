package com.example.clinic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clinic.R
import com.example.clinic.data.CommonQuestions

class ExpandableQuestionsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<ExpandableQuestionItem> = mutableListOf()
    private var allQuestions: List<CommonQuestions> = emptyList()

    companion object {
        private const val VIEW_TYPE_PARENT = 0
        private const val VIEW_TYPE_CHILD = 1
    }

    fun submitQuestions(questions: List<CommonQuestions>) {
        allQuestions = questions
        refreshDataList()
    }

    private fun refreshDataList() {
        val oldExpandedStates = dataList.filterIsInstance<ExpandableQuestionItem.Parent>()
            .associate { it.question.id to it.isExpanded }

        dataList.clear()
        allQuestions.forEach { question ->
            val parentItem = ExpandableQuestionItem.Parent(question, oldExpandedStates[question.id] ?: false)
            dataList.add(parentItem)
            if (parentItem.isExpanded) {
                dataList.add(ExpandableQuestionItem.Child(question.answer, question.id))
            }
        }
        notifyDataSetChanged() // For a full refresh (can be optimized with DiffUtil)
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataList[position]) {
            is ExpandableQuestionItem.Parent -> VIEW_TYPE_PARENT
            is ExpandableQuestionItem.Child -> VIEW_TYPE_CHILD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_PARENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq_question, parent, false)
                ParentViewHolder(view)
            }
            VIEW_TYPE_CHILD -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq_answer, parent, false)
                ChildViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_PARENT -> {
                val parentHolder = holder as ParentViewHolder
                val parentItem = dataList[position] as ExpandableQuestionItem.Parent
                parentHolder.bind(parentItem)

                parentHolder.itemView.setOnClickListener {
                    toggleParentExpansion(parentItem, position)
                }
            }
            VIEW_TYPE_CHILD -> {
                val childHolder = holder as ChildViewHolder
                val childItem = dataList[position] as ExpandableQuestionItem.Child
                childHolder.bind(childItem)
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

    inner class ParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionTextView: TextView = itemView.findViewById(R.id.tv_question)
        private val arrowImageView: ImageView = itemView.findViewById(R.id.iv_expand_arrow)

        fun bind(parentItem: ExpandableQuestionItem.Parent) {
            questionTextView.text = parentItem.question.question
            arrowImageView.setImageResource(
                if (parentItem.isExpanded) R.drawable.up_arrow else R.drawable.down_b_arrow
            )
        }
    }

    inner class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val answerTextView: TextView = itemView.findViewById(R.id.tv_answer)

        fun bind(childItem: ExpandableQuestionItem.Child) {
            answerTextView.text = childItem.answer
        }
    }

    private fun toggleParentExpansion(parentItem: ExpandableQuestionItem.Parent, position: Int) {
        parentItem.isExpanded = !parentItem.isExpanded

        if (parentItem.isExpanded) {
            // Insert child (answer) right after the parent
            dataList.add(position + 1, ExpandableQuestionItem.Child(parentItem.question.answer, parentItem.question.id))
            notifyItemInserted(position + 1)
        } else {
            // Safety check: ensure the item at position+1 is indeed the child of this parent
            if (position + 1 < dataList.size && dataList[position + 1] is ExpandableQuestionItem.Child) {
                val child = dataList[position + 1] as ExpandableQuestionItem.Child
                if (child.parentId == parentItem.question.id) { // Verify it's the correct child
                    dataList.removeAt(position + 1)
                    notifyItemRemoved(position + 1)
                }
            }
        }
        notifyItemChanged(position) // Update the parent item's arrow
    }
}