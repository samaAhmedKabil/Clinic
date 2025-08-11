package com.example.clinic.ui.settings.prices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clinic.data.Service
import com.example.clinic.repos.ServicesRepo

class ServicesViewModel(private val repository: ServicesRepo) : ViewModel() {

    private val _isDoctor = MutableLiveData<Boolean>()
    val isDoctor: LiveData<Boolean> get() = _isDoctor

    private val _services = MutableLiveData<List<Service>>()
    val services: LiveData<List<Service>> get() = _services

    fun checkUserRole() {
        repository.getUserRole { role ->
            _isDoctor.value = role
        }
    }

    fun loadServices() {
        repository.loadServices { list ->
            _services.value = list
        }
    }

    fun saveService(service: Service) {
        repository.saveService(service)
    }
}

class ServicesViewModelFactory(private val repository: ServicesRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServicesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServicesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
