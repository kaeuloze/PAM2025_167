package com.example.mykapal.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykapal.data.model.CrewCertification
import com.example.mykapal.data.repository.CrewCertificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CrewCertificationViewModel : ViewModel() {

    private val repo = CrewCertificationRepository()

    private val _list = MutableStateFlow<List<CrewCertification>>(emptyList())
    val list: StateFlow<List<CrewCertification>> = _list

    var showDialog by mutableStateOf(false)
    var selected by mutableStateOf<CrewCertification?>(null)

    fun load() {
        viewModelScope.launch {
            _list.value = repo.getAll()
        }
    }

    fun save(data: CrewCertification) {
        viewModelScope.launch {
            if (data.crew_cert_id == 0)
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
