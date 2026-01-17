package com.example.mykapal.viewmodel

import android.app.Notification
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykapal.data.model.DashboardSummary
import com.example.mykapal.data.model.NotificationModel
import com.example.mykapal.data.repository.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private val DashboardSummary.data: DashboardSummary?
    get() {
        TODO()
    }
private val List<NotificationModel>.data: Any
    get() {
        TODO()
    }
private val List<NotificationModel>.success: Boolean
    get() {
        TODO()
    }
private val DashboardSummary.success: Boolean
    get() {
        TODO()
    }

class DashboardViewModel : ViewModel() {
    private val repo = DashboardRepository()

    private val _summary = MutableStateFlow<DashboardSummary?>(null)
    val summary: StateFlow<DashboardSummary?> = _summary.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationModel>>(emptyList())
    val notifications: StateFlow<List<NotificationModel>> = _notifications.asStateFlow()

    // State untuk loading & error
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadDashboard() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Load summary
                val summaryResponse = repo.getSummary()
                if (summaryResponse.success) {
                    _summary.value = summaryResponse.data as DashboardSummary? // ← AMBIL DATA
                }

                // Load notifications
                val notifResponse = repo.getNotification()
                if (notifResponse.success) {
                    _notifications.value =
                        notifResponse.data as List<NotificationModel> // ← AMBIL DATA
                }

            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}