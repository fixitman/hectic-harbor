package com.example.logintest.ui


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.dataaccess.ReminderAPIService
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(
) {
    var reminders = mutableStateListOf<com.example.logintest.dataaccess.Reminder>()


    fun getReminders(){
        viewModelScope.launch {
            val fetchedReminders  = ReminderAPIService().getAllReminders()
            if(fetchedReminders.isNotEmpty()){
                reminders.clear()
                fetchedReminders.forEach {
                    reminders.add(it)
                }                
            }
        }
    }
}

