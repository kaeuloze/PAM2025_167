package com.example.mykapal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykapal.data.model.Certification
import com.example.mykapal.data.model.Crew
import com.example.mykapal.data.model.CrewCertification
import com.example.mykapal.data.repository.CertificationRepository
import com.example.mykapal.data.repository.CrewCertificationRepository
import com.example.mykapal.data.repository.CrewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CrewCertificationViewModel : ViewModel() {

    private val repo = CrewCertificationRepository()
    private val certificationRepo = CertificationRepository()
    private val crewRepo = CrewRepository()

    // ===== LIST DATA =====
    private val _list = MutableStateFlow<List<CrewCertification>>(emptyList())
    val list: StateFlow<List<CrewCertification>> = _list.asStateFlow()

    private val _certList = MutableStateFlow<List<Certification>>(emptyList())
    val certList: StateFlow<List<Certification>> = _certList.asStateFlow()

    private val _crewList = MutableStateFlow<List<Crew>>(emptyList())
    val crewList: StateFlow<List<Crew>> = _crewList.asStateFlow()

    // ===== LOADING & ERROR =====
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // ===== DIALOG STATE (INI YANG DIPAKAI collectAsState) =====
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _selectedItem = MutableStateFlow<CrewCertification?>(null)
    val selectedItem: StateFlow<CrewCertification?> = _selectedItem.asStateFlow()

    // ===== SEARCH FLOW =====
    private val _searchQueryFlow = MutableStateFlow("")
    val searchQueryFlow: StateFlow<String> = _searchQueryFlow.asStateFlow()

    // ===== FILTERED LIST =====
    val filteredList: StateFlow<List<CrewCertification>> = combine(
        _list,
        _searchQueryFlow
    ) { list, query ->
        if (query.isBlank()) list
        else {
            list.filter { item ->
                item.nama_sertifikasi.contains(query, ignoreCase = true) ||
                        item.kru.contains(query, ignoreCase = true) ||
                        item.status.contains(query, ignoreCase = true) ||
                        item.tanggal_terbit.contains(query, ignoreCase = true) ||
                        item.tanggal_kadaluarsa.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        load()
        loadMasterData()
    }

    // ===== LOAD MASTER DATA (dropdown dialog) =====
    fun loadMasterData() {
        viewModelScope.launch {
            try {
                val certRes = certificationRepo.getAll()
                if (certRes.success) _certList.value = certRes.data ?: emptyList()

                val crewRes = crewRepo.getAll()
                if (crewRes.success) _crewList.value = crewRes.data ?: emptyList()

            } catch (_: Exception) {
                // master data boleh gagal tanpa crash
            }
        }
    }

    // ===== LOAD LIST =====
    fun load() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val res = repo.getAll()
                if (res.success) {
                    _list.value = res.data ?: emptyList()
                } else {
                    _errorMessage.value = "Gagal memuat data: ${res.message ?: "Unknown error"}"
                    _list.value = emptyList()
                }

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _errorMessage.value = "HTTP ${e.code()} : ${errorBody ?: "Tidak ada pesan dari server"}"
                _list.value = emptyList()

            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
                _list.value = emptyList()

            } finally {
                _isLoading.value = false
            }
        }
    }

    // ===== SAVE (INSERT / UPDATE) =====
    fun save(data: CrewCertification) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val res = if (data.crew_cert_id == 0) repo.insert(data) else repo.update(data)

                if (res.success) {
                    load()
                    closeDialog()
                } else {
                    _errorMessage.value = "Gagal menyimpan data: ${res.message ?: "Unknown error"}"
                }

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _errorMessage.value = "HTTP ${e.code()} : ${errorBody ?: "Tidak ada pesan dari server"}"

            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"

            } finally {
                _isLoading.value = false
            }
        }
    }

    // ===== DELETE =====
    fun delete(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val res = repo.delete(id)
                if (res.success) {
                    load()
                } else {
                    _errorMessage.value = "Gagal menghapus data: ${res.message ?: "Unknown error"}"
                }

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _errorMessage.value = "HTTP ${e.code()} : ${errorBody ?: "Tidak ada pesan dari server"}"

            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"

            } finally {
                _isLoading.value = false
            }
        }
    }

    // ===== SEARCH =====
    fun updateSearchQuery(query: String) {
        _searchQueryFlow.value = query
    }

    // ===== DIALOG CONTROL (pakai Flow) =====
    fun openDialog(item: CrewCertification? = null) {
        _selectedItem.value = item
        _showDialog.value = true
    }

    fun closeDialog() {
        _showDialog.value = false
        _selectedItem.value = null
    }

    // kalau kamu klik edit dari item list
    fun setSelectedItem(item: CrewCertification) {
        _selectedItem.value = item
        _showDialog.value = true
    }

    // ===== ERROR CONTROL =====
    fun clearError() {
        _errorMessage.value = null
    }

    // ===== SORT (optional) =====
    fun sortBy(field: SortField) {
        val sorted = when (field) {
            SortField.KRU -> _list.value.sortedBy { it.kru }
            SortField.SERTIFIKASI -> _list.value.sortedBy { it.nama_sertifikasi }
            SortField.EXPIRY -> _list.value.sortedBy { it.tanggal_kadaluarsa }
        }
        _list.value = sorted
    }

    enum class SortField {
        KRU, SERTIFIKASI, EXPIRY
    }
}
