package com.example.logintest

import android.app.Application
import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.exitProcess

class MainViewModel(application: Application) : AndroidViewModel(application)
{

    private var waitingForLogin: Boolean = false
    var reminders = mutableStateListOf<Reminder>()
    //private var credManager = CredentialManager(getApplication<Application>().applicationContext) //{getCreds()}
    var showLoginDialog by mutableStateOf(false)
    var isLoading: Boolean  by mutableStateOf(true)
    val credentials = mutableStateOf(LoginModel(UserName = "", password = "", remember = false))
    val reminderService = ReminderAPIService(token = "")


    init {
        getReminders()
    }

    fun getReminders(){
        viewModelScope.launch {
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

//    private fun getCreds(): LoginModel  {
//
//        viewModelScope.launch(Dispatchers.Main) {
//             showLoginDialog = true
//        }
//
//        waitingForLogin = true
//        while(waitingForLogin){}
//        showLoginDialog = false
//
//        val ret = credentials.value.copy()
//        resetCredentials()
//        return ret
//    }

//    private fun resetCredentials(){
//        credentials.value.let{
//            it.UserName = ""
//            it.password= ""
//            it.remember = false
//        }
//
//    }

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
        waitingForLogin = false
    }

    fun onExit() {
        exitProcess(0)
    }

    fun onReminderClick(reminder: Reminder) {
        Log.d(TAG, "onReminderClick: ${reminder.reminderTime}")
    }

    suspend fun getReminder(id: Int): Reminder? {
        return reminderService.getReminder(id)

    }


}

