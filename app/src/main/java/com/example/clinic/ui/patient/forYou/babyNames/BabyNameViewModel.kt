package com.example.clinic.ui.patient.forYou.babyNames

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clinic.data.BabyName
import com.example.clinic.repos.BabyNamesRepo

class BabyNameViewModel : ViewModel() {

    private val repo = BabyNamesRepo()
    private val _babyNames = MutableLiveData<List<BabyName>>()
    val babyNames: LiveData<List<BabyName>> = _babyNames

    private var allNames: List<BabyName> = emptyList()

    private var selectedGender: String = "boy"

    init {
        repo.getBabyNames { names ->
            allNames = names
            applyFilter()
        }
    }

    fun addBabyName(name: String, gender: String) {
        repo.addBabyName(name, gender)
    }

    fun filterByGender(gender: String) {
        selectedGender = gender
        applyFilter()
    }

    private fun applyFilter() {
        _babyNames.value = if (selectedGender.isEmpty()) {
            allNames
        } else {
            allNames.filter { it.gender.equals(selectedGender, ignoreCase = true) }
        }
    }

}