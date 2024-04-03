package com.example.logintest.ui

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.dataaccess.CredentialManager
import com.example.logintest.dataaccess.LoginModel
import com.example.logintest.dataaccess.ReminderAPIService
import com.example.logintest.utils.Secret
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application)
{

    var reminders = mutableStateListOf<com.example.logintest.dataaccess.Reminder>()
    var credManager: CredentialManager = CredentialManager(getApplication<Application>().applicationContext) {getCreds()}
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

    fun getCreds(): LoginModel  {

        showLoginDialog = true
        return LoginModel(Secret.USERNAME, Secret.PASSWORD)

    }

    fun submitLogin() {
        showLoginDialog = false

    }

    fun dismissLogin(){
        showLoginDialog = false
    }

    fun resetCredentials(){
        credentials.value.let{
            it.UserName = ""
            it.password= ""
            it.remember = false
        }

    }


}

