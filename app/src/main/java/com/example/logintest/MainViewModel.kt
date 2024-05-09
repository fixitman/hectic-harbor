package com.example.logintest

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.dataaccess.CredentialManager
import com.example.logintest.dataaccess.LoginModel
import com.example.logintest.dataaccess.Reminder
import com.example.logintest.dataaccess.ReminderAPIService
import com.example.logintest.utils.Strings.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    var isLoading: Boolean  by mutableStateOf(true)
        private set
    var showAuthScreen by mutableStateOf(true)
        private set
    val credentials = mutableStateOf(LoginModel(UserName = "", password = "", remember = false))

    private val _uiEvents = MutableSharedFlow<UIEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

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

                    is CredentialManager.Event.ErrorEvent -> {
                        _uiEvents.emit(UIEvent.SnackbarEvent(event.message))
                    }
                }
            }
        }
    }

    fun getReminders(){
        viewModelScope.launch(IO) {
            withContext(Main) {isLoading = true}

            val fetchedReminders  = reminderService.getAllReminders()
            if(fetchedReminders.isNotEmpty()){
                reminders.clear()
                fetchedReminders.forEach {
                    reminders.add(it)
                }
            }

            withContext(Main) {isLoading = false}
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

    fun onAuthDone() {
        showAuthScreen = false
    }

    suspend fun getReminder(id: Int): Reminder? {
        isLoading = true

        currentReminder = reminderService.getReminder(id)
        isLoading = false

        return currentReminder

    }

    fun updateToken(token: String?) {
        if(token != null){
            reminderService = ReminderAPIService(token)
            showAuthScreen = false
        }
    }

    suspend fun getToken(): String? {
        return credentialManager.getToken()
    }

    fun showSnackbar(message: String){
        viewModelScope.launch {
            _uiEvents.emit(UIEvent.SnackbarEvent(message))
        }
    }

    sealed interface UIEvent{
        data class SnackbarEvent(val message: String) : UIEvent
    }


}

