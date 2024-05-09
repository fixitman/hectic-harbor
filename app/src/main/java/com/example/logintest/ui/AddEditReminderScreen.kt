package com.example.logintest.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logintest.MainViewModel
import com.example.logintest.dataaccess.Reminder
import com.example.logintest.utils.Strings.TAG

@Composable
fun AddEditReminderScreen(
    id: Int,
    viewModel: MainViewModel = viewModel()
) {
    var reminder: Reminder? = viewModel.currentReminder
    LaunchedEffect(key1 = id) {
        reminder = viewModel.getReminder(id)
        Log.d(TAG, "AddEditReminder: $reminder")
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .background(colorScheme.primary)
    ){
        Text(
           text = reminder?.reminderText ?: "Not found",
            style = typography.titleMedium,
            color = colorScheme.onPrimary
        )
    }
}





