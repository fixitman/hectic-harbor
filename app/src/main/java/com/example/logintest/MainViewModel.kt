package com.example.logintest

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.dataaccess.CredentialManager
import com.example.logintest.dataaccess.LoginModel
import com.example.logintest.dataaccess.Reminder
import com.example.logintest.dataaccess.ReminderAPIService
import com.example.logintest.utils.Strings.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltViewModel
class MainViewModel @Inject constructor (
    val credentialManager: CredentialManager
) : ViewModel()
{
    var currentReminder: Reminder? = null
    var reminders = mutableStateListOf<Reminder>()
    var showLoginDialog by mutableStateOf(false)
        private set
    var showAuth by mutableStateOf(true)
    var isLoading: Boolean  by mutableStateOf(true)
    val credentials = mutableStateOf(LoginModel(UserName = "", password = "", remember = false))
    lateinit var reminderService: ReminderAPIService

    init {
        viewModelScope.launch {
            credentialManager.events.collect{event ->
                when(event){
                    is CredentialManager.Event.TokenChangedEvent -> {
                        updateToken(event.token)
                        getReminders()
                    }
                    CredentialManager.Event.InvalidCredentialsEvent -> {
                        showLoginDialog = true
                    }
                }
            }
        }
    }

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
        credentialManager.updateCreds(credentials.value)
        resetCredentials()
        showLoginDialog = false

        viewModelScope.launch(IO) {
            credentialManager.getToken()
        }
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
        if(token != null){
            reminderService = ReminderAPIService(token)
            showAuth = false
        }
    }

    suspend fun getToken(): String? {
        return credentialManager.getToken()
    }
}

