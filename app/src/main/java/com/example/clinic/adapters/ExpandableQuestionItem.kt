package com.example.clinic.adapters

import com.example.clinic.data.CommonQuestions

sealed class ExpandableQuestionItem {
    data class Parent(val question: CommonQuestions, var isExpanded: Boolean = false) : ExpandableQuestionItem()
    data class Child(val answer: String, val parentId: String) : ExpandableQuestionItem()
}