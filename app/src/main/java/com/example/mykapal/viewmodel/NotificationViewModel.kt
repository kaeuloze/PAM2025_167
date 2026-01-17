package com.example.mykapal.viewmodel

import android.app.Notification
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykapal.data.model.NotificationModel
import com.example.mykapal.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    private val repo = NotificationRepository()

    private val _list = MutableStateFlow<List<NotificationModel>>(emptyList())
    val list: StateFlow<List<NotificationModel>> = _list

    fun load() {
        viewModelScope.launch {
            _list.value = repo.getAll()
        }
    }
}