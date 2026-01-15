package com.example.mykapal.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykapal.data.model.Certification
import com.example.mykapal.data.repository.CertificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CertificationViewModel : ViewModel() {

    private val repo = CertificationRepository()

    private val _list = MutableStateFlow<List<Certification>>(emptyList())
    val list: StateFlow<List<Certification>> = _list

    var showDialog by mutableStateOf(false)
    var selected by mutableStateOf<Certification?>(null)

    fun load() {
        viewModelScope.launch {
            _list.value = repo.getAll()
        }
    }

    fun save(data: Certification) {
        viewModelScope.launch {
            if (data.sertif_id == 0)
                repo.insert(data)
            else
                repo.update(data)

            load()
            showDialog = false
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            repo.delete(id)
            load()
        }
    }
}
