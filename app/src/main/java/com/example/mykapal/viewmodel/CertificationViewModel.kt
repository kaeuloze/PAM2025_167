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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CertificationViewModel : ViewModel() {
    private val repo = CertificationRepository()

    private val _list = MutableStateFlow<List<Certification>>(emptyList())
    val list: StateFlow<List<Certification>> = _list.asStateFlow()

    // State untuk loading & error
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    var showDialog by mutableStateOf(false)
    var selected by mutableStateOf<Certification?>(null)

    fun load() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = repo.getAll() // ApiResponse<List<Certification>>
                if (response.success) {
                    _list.value = response.data ?: emptyList()
                    // ← AMBIL DATA, BUKAN RESPONSE
                } else {
                    _errorMessage.value = "Gagal memuat data"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
                _list.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun save(data: Certification) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = if (data.sertif_id == 0) {
                    repo.insert(data)
                } else {
                    repo.update(data)
                }

                if (response.success) {
                    load()
                    showDialog = false
                    selected = null
                } else {
                    _errorMessage.value = response.message ?: "Gagal menyimpan data"
                }

            } catch (e: HttpException) {
                val err = e.response()?.errorBody()?.string()
                _errorMessage.value = "HTTP ${e.code()} : $err"

            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"

            } finally {
                _isLoading.value = false
            }
        }
    }


    fun delete(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repo.delete(id)
                if (response.success) {
                    load()
                } else {
                    _errorMessage.value = "Gagal menghapus data"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}