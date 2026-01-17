package com.example.mykapal.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykapal.data.model.Crew
import com.example.mykapal.data.repository.CrewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*
import retrofit2.HttpException

class CrewViewModel : ViewModel() {
    private val repo = CrewRepository()

    // State untuk crew list
    private val _crewList = MutableStateFlow<List<Crew>>(emptyList())
    val crewList: StateFlow<List<Crew>> = _crewList.asStateFlow()

    // State untuk loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State untuk error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // State untuk UI yang perlu diakses di Composable
    var searchQuery by mutableStateOf("")
    var showDialog by mutableStateOf(false)
    var selectedCrew by mutableStateOf<Crew?>(null)

    // State untuk search query yang reactive (alternatif)
    private val _searchQueryFlow = MutableStateFlow("")
    val searchQueryFlow: StateFlow<String> = _searchQueryFlow.asStateFlow()

    // State untuk filtered crew yang reactive
    val filteredCrew: StateFlow<List<Crew>> = combine(
        _crewList,
        _searchQueryFlow
    ) { crewList, searchQuery ->
        if (searchQuery.isBlank()) {
            crewList
        } else {
            crewList.filter { crew ->
                crew.nama_lengkap.contains(searchQuery, ignoreCase = true) ||
                        crew.posisi.contains(searchQuery, ignoreCase = true) ||
                        crew.nomor_passport.contains(searchQuery, ignoreCase = true) ||
                        crew.kewarganegaraan.contains(searchQuery, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadCrew()
    }

    fun loadCrew() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = repo.getAll() // ApiResponse<List<Crew>>
                if (response.success) {
                    _crewList.value = response.data ?: emptyList()

                } else {
                    _errorMessage.value = "Gagal memuat data crew: ${response.message}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
                _crewList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveCrew(crew: Crew) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = if (crew.crew_id == 0) {
                    repo.insert(crew)
                } else {
                    repo.update(crew)
                }

                if (response.success) {
                    loadCrew() // reload data
                    showDialog = false
                    selectedCrew = null
                } else {
                    _errorMessage.value =
                        "Gagal menyimpan data crew: ${response.message ?: "Unknown error"}"
                }

            } catch (e: HttpException) {
                // 🔴 INI YANG MENAMPILKAN ERROR 400 / 500 DARI SERVER
                val errorBody = e.response()?.errorBody()?.string()
                _errorMessage.value =
                    "HTTP ${e.code()} : ${errorBody ?: "Tidak ada pesan dari server"}"

            } catch (e: Exception) {
                // error lain (network, parsing, dll)
                _errorMessage.value = "Error: ${e.message}"

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteCrew(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = repo.delete(id)
                if (response.success) {
                    loadCrew() // Reload data setelah delete
                } else {
                    _errorMessage.value = "Gagal menghapus data crew: ${response.message}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Fungsi untuk update search query (jika pakai reactive search)
    fun updateSearchQuery(query: String) {
        _searchQueryFlow.value = query
    }

    // Fungsi untuk open dialog dengan atau tanpa data
    fun openDialog(crew: Crew? = null) {
        selectedCrew = crew
        showDialog = true
    }

    // Fungsi untuk close dialog
    fun closeDialog() {
        showDialog = false
        selectedCrew = null
    }

    // Fungsi untuk clear error message
    fun clearError() {
        _errorMessage.value = null
    }

    // Fungsi untuk sort crew (optional)
    fun sortBy(field: SortField) {
        val sortedList = when (field) {
            SortField.NAME -> _crewList.value.sortedBy { it.nama_lengkap }
            SortField.POSITION -> _crewList.value.sortedBy { it.posisi }
            SortField.AGE -> _crewList.value.sortedByDescending {
                calculateAge(it.tanggal_lahir)
            }
        }
        _crewList.value = sortedList
    }

    // Fungsi untuk export data (optional)
    suspend fun exportCrewData(): String {
        return _crewList.value.joinToString("\n") { crew ->
            "${crew.nama_lengkap}, ${crew.posisi}, ${crew.nomor_passport}"
        }
    }

    // Helper function untuk calculate age
    private fun calculateAge(birthDate: String): Int {
        return try {
            val birth = java.time.LocalDate.parse(birthDate)
            val current = java.time.LocalDate.now()
            java.time.Period.between(birth, current).years
        } catch (e: Exception) {
            0
        }
    }

    // Enum untuk sorting
    enum class SortField {
        NAME, POSITION, AGE
    }
}