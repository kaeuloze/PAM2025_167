package com.example.mykapal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykapal.data.model.CrewShift
import com.example.mykapal.data.repository.CrewShiftRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CrewShiftViewModel : ViewModel() {

    private val repo = CrewShiftRepository()

    private val _list = MutableStateFlow<List<CrewShift>>(emptyList())
    val list: StateFlow<List<CrewShift>> = _list

    var showDialog = false
    var selected: CrewShift? = null

    fun load() {
        viewModelScope.launch {
            val response = repo.getAll()
            _list.value = response.data ?: emptyList()

        }
    }

    fun save(data: CrewShift) {
        viewModelScope.launch {
            if (data.crew_shift_id == 0)
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
