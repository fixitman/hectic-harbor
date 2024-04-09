package com.example.logintest.ui

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.dataaccess.CredentialManager
import com.example.logintest.dataaccess.LoginModel
import com.example.logintest.dataaccess.Reminder
import com.example.logintest.dataaccess.ReminderAPIService
import com.example.logintest.utils.Strings.TAG
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class MainViewModel(application: Application) : AndroidViewModel(application)
{

    private var waiting: Boolean = false
    var reminders = mutableStateListOf<Reminder>()
    private var credManager: CredentialManager = CredentialManager(getApplication<Application>().applicationContext) {getCreds()}
    var showLoginDialog by mutableStateOf(false)
    val credentials: MutableState<LoginModel> = mutableStateOf(LoginModel(UserName = "", password = "", remember = false))



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

    private fun getCreds(): LoginModel  {

        showLoginDialog = true

        waiting = true
        while(waiting){}
        showLoginDialog = false

        val ret = credentials.value.copy()
        resetCredentials()
        return ret
    }

    private fun resetCredentials(){
        credentials.value.let{
            it.UserName = ""
            it.password= ""
            it.remember = false
        }

    }

    fun submitLogin() {
        //showLoginDialog = false
        waiting = false

    }

    fun updateUser(s: String) {
        credentials.value = credentials.value.copy(UserName = s)
    }

    fun updatePassword(s: String) {
        credentials.value = credentials.value.copy(password = s)
    }

    fun updateRemember(b: Boolean) {
        credentials.value = credentials.value.copy(remember = b)
    }

    fun onExit() {
        exitProcess(0)
    }

    fun onReminderClick(reminder: Reminder) {
        Log.d(TAG, "onReminderClick: ${reminder.reminderTime}")
    }


}

