package com.example.clinic.data

data class BabyKicks(
    val kicksCount: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)