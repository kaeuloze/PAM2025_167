package com.example.mykapal.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykapal.data.model.Shift
import com.example.mykapal.data.repository.ShiftRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ShiftViewModel : ViewModel() {
    private val repo = ShiftRepository()

    // ===== LIST =====
    private val _shiftList = MutableStateFlow<List<Shift>>(emptyList())
    val shiftList: StateFlow<List<Shift>> = _shiftList.asStateFlow()

    // ===== LOADING =====
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // ===== ERROR =====
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // ===== UI STATE =====
    var searchQuery by mutableStateOf("")
    var showDialog by mutableStateOf(false)
    var selectedShift by mutableStateOf<Shift?>(null)

    // Reactive search query
    private val _searchQueryFlow = MutableStateFlow("")
    val searchQueryFlow: StateFlow<String> = _searchQueryFlow.asStateFlow()

    // Filtered list
    val filteredShift: StateFlow<List<Shift>> = combine(
        _shiftList,
        _searchQueryFlow
    ) { list, query ->
        val q = query.trim()
        if (q.isBlank()) {
            list
        } else {
            list.filter { s ->
                s.namaShift.contains(q, ignoreCase = true) ||
                        s.jam_mulai.contains(q, ignoreCase = true) ||
                        s.jam_selesai.contains(q, ignoreCase = true) ||
                        (s.deskripsi?.contains(q, ignoreCase = true) == true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadShifts()
    }

    fun loadShifts() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = repo.get() // ApiResponse<List<Shift>>
                if (response.success) {
                    _shiftList.value = response.data ?: emptyList()
                } else {
                    _errorMessage.value = "Gagal memuat data shift: ${response.message ?: "Unknown error"}"
                    _shiftList.value = emptyList()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
                _shiftList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveShift(shift: Shift) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = if (shift.shift_id == 0) {
                    repo.insert(shift)
                } else {
                    repo.update(shift)
                }

                if (response.success) {
                    loadShifts()
                    showDialog = false
                    selectedShift = null
                } else {
                    _errorMessage.value =
                        "Gagal menyimpan data shift: ${response.message ?: "Unknown error"}"
                }

            } catch (e: HttpException) {
                // tampilkan error body dari server (400/500)
                val errorBody = e.response()?.errorBody()?.string()
                _errorMessage.value =
                    "HTTP ${e.code()} : ${errorBody ?: "Tidak ada pesan dari server"}"

            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteShift(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = repo.delete(id)
                if (response.success) {
                    loadShifts()
                } else {
                    _errorMessage.value = "Gagal menghapus shift: ${response.message ?: "Unknown error"}"
                }

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _errorMessage.value =
                    "HTTP ${e.code()} : ${errorBody ?: "Tidak ada pesan dari server"}"

            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"

            } finally {
                _isLoading.value = false
            }
        }
    }

    // ===== UI HELPERS =====
    fun updateSearchQuery(query: String) {
        searchQuery = query
        _searchQueryFlow.value = query
    }

    fun openDialog(shift: Shift? = null) {
        selectedShift = shift
        showDialog = true
    }

    fun openAddDialog() = openDialog(null)

    fun openEditDialog(shift: Shift) = openDialog(shift)

    fun closeDialog() {
        showDialog = false
        selectedShift = null
    }

    fun clearError() {
        _errorMessage.value = null
    }

    // Optional sort
    fun sortBy(field: SortField) {
        _shiftList.value = when (field) {
            SortField.NAME -> _shiftList.value.sortedBy { it.namaShift }
            SortField.START -> _shiftList.value.sortedBy { it.jam_mulai }
            SortField.END -> _shiftList.value.sortedBy { it.jam_selesai }
        }
    }

    enum class SortField { NAME, START, END }
}
