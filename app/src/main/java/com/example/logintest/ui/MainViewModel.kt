package com.example.logintest.ui

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.dataaccess.CredManager
import com.example.logintest.dataaccess.LoginModel
import com.example.logintest.dataaccess.ReminderAPIService
import com.example.logintest.utils.Secret
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application)
{
    var reminders = mutableStateListOf<com.example.logintest.dataaccess.Reminder>()
    var credManager: CredManager = CredManager(getApplication<Application>().applicationContext) { getCreds() }


    fun getReminders(){
        viewModelScope.launch {
            val fetchedReminders  = ReminderAPIService(credManager).getAllReminders()
            if(fetchedReminders.isNotEmpty()){
                reminders.clear()
                fetchedReminders.forEach {
                    reminders.add(it)
                }
            }
        }
    }

    fun getCreds(): LoginModel?  {
        //return null
        return LoginModel(Secret.USERNAME, Secret.PASSWORD)
    }
}

