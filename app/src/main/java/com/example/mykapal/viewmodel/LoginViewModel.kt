package com.example.mykapal.viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykapal.data.repository.AuthRepository
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var loginSuccess by mutableStateOf(false)

    private val repo = AuthRepository()

    fun login() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = repo.login(username, password)

                if (response.success) {
                    loginSuccess = true
                } else {
                    errorMessage = response.message
                }

            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}