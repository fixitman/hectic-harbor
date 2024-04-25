package com.example.logintest

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.dataaccess.LoginModel
import com.example.logintest.dataaccess.Reminder
import com.example.logintest.dataaccess.ReminderAPIService
import com.example.logintest.utils.Strings.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.exitProcess

class MainViewModel(application: Application) : AndroidViewModel(application)
{

    var currentReminder: Reminder? = null
    var token: String? = null
    private var waitingForCredsDlgInput: Boolean = false
    var reminders = mutableStateListOf<Reminder>()
    var showLoginDialog by mutableStateOf(false)
    var isLoading: Boolean  by mutableStateOf(true)
    val credentials = mutableStateOf(LoginModel(UserName = "", password = "", remember = false))
    lateinit var reminderService: ReminderAPIService


    fun getReminders(){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {isLoading = true}

            val fetchedReminders  = reminderService.getAllReminders()
            if(fetchedReminders.isNotEmpty()){
                reminders.clear()
                fetchedReminders.forEach {
                    reminders.add(it)
                }
            }

            withContext(Dispatchers.Main) {isLoading = false}
        }
    }

    fun askForCreds(): LoginModel  {

        viewModelScope.launch(Dispatchers.Main) {
             showLoginDialog = true
        }

        waitingForCredsDlgInput = true
        while(waitingForCredsDlgInput){}
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

    fun updateUser(s: String) {
        credentials.value = credentials.value.copy(UserName = s)
    }

    fun updatePassword(s: String) {
        credentials.value = credentials.value.copy(password = s)
    }

    fun updateRemember(b: Boolean) {
        credentials.value = credentials.value.copy(remember = b)
    }

    fun onSubmit() {
        waitingForCredsDlgInput = false
    }

    fun onExit() {
        exitProcess(0)
    }

    fun onReminderClick(reminder: Reminder) {
        Log.d(TAG, "onReminderClick: ${reminder.reminderTime}")
    }

    suspend fun getReminder(id: Int): Reminder? {
        return reminderService.getReminder(id).also {
            currentReminder = it
        }

    }

    fun updateToken(token: String?) {
        this.token = token
        if(token != null){
            Log.d(TAG, "updateToken: ")
            reminderService = ReminderAPIService(token)
        }
    }


}

