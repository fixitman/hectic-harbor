package com.example.logintest.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logintest.MainViewModel
import com.example.logintest.dataaccess.CredentialManager
import com.example.logintest.dataaccess.Reminder
import com.example.logintest.dataaccess.ReminderAPIService
import com.example.logintest.utils.Strings.TAG

@Composable
fun AddEditReminder(
    id: Int,
    viewModel: AddEditReminderViewModel = viewModel(),
    context: Context = LocalContext.current

) {
    var reminder : Reminder?
    LaunchedEffect(key1 = id) {
        reminder = viewModel.getReminder(id)
        Log.d(TAG, "AddEditReminder: $reminder")
    }
}

class AddEditReminderViewModel(
    private val application: Application
): AndroidViewModel(application)
{
    val reminderService: ReminderAPIService = ReminderAPIService("FixMe!")


    fun getReminder(id: Int): Reminder? {
        TODO("Not yet implemented")
    }


}
