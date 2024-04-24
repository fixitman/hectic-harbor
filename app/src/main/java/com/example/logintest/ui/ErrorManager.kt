package com.example.logintest.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ErrorManager(
    val snackbarHostState: SnackbarHostState,
    val scope: CoroutineScope
) {
    fun showError(msg: String){
        scope.launch {
            snackbarHostState.showSnackbar(message = msg, duration = SnackbarDuration.Short)
        }
    }
}